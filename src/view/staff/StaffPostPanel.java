package view.staff;

import controller.staff.StaffController;
import models.Post;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StaffPostPanel extends JPanel {
    private StaffController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    public StaffPostPanel(StaffController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(UIUtils.LIGHT_BG);

        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadData();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIUtils.LIGHT_BG);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Quản Lý Bài Viết");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIUtils.PRIMARY_COLOR);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controls.setBackground(UIUtils.LIGHT_BG);

        JButton btnAdd = new JButton("Thêm Bài Viết");
        btnAdd.setBackground(UIUtils.PRIMARY_COLOR);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> showEditDialog(null));

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadData());

        controls.add(btnAdd);
        controls.add(btnRefresh);

        header.add(title, BorderLayout.WEST);
        header.add(controls, BorderLayout.EAST);
        return header;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = { "ID", "Tiêu đề", "Danh mục", "Trạng thái", "Ngày đăng" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Double click to edit
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int id = (int) table.getValueAt(table.getSelectedRow(), 0);
                    showEditDialog(getPostById(id));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private Post getPostById(int id) {
        for (Post p : controller.getAllPosts()) {
            if (p.getId() == id)
                return p;
        }
        return null;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Post> list = controller.getAllPosts();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Post p : list) {
            Object[] row = {
                    p.getId(),
                    p.getTitle(),
                    p.getCategory(),
                    p.getStatus(),
                    p.getPublishedAt() != null ? p.getPublishedAt().format(formatter) : ""
            };
            tableModel.addRow(row);
        }
    }

    private void showEditDialog(Post post) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                post == null ? "Thêm Bài Viết Mới" : "Sửa Bài Viết", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        form.add(new JLabel("Tiêu đề:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        JTextField txtTitle = new JTextField(post != null ? post.getTitle() : "");
        form.add(txtTitle, gbc);

        // Category
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.2;
        form.add(new JLabel("Danh mục:"), gbc);
        gbc.gridx = 1;
        String[] cats = { "Tin tức", "Thông báo", "Bí quyết", "Sự kiện" };
        JComboBox<String> cbCategory = new JComboBox<>(cats);
        if (post != null)
            cbCategory.setSelectedItem(post.getCategory());
        form.add(cbCategory, gbc);

        // Content
        gbc.gridx = 0;
        gbc.gridy++;
        form.add(new JLabel("Nội dung:"), gbc);
        gbc.gridx = 1;
        JTextArea txtContent = new JTextArea(post != null ? post.getContent() : "");
        txtContent.setRows(10);
        txtContent.setLineWrap(true);
        form.add(new JScrollPane(txtContent), gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy++;
        form.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        String[] statuses = { "Bản nháp", "Đã đăng", "Lưu trữ" };
        JComboBox<String> cbStatus = new JComboBox<>(statuses);
        if (post != null)
            cbStatus.setSelectedItem(post.getStatus());
        form.add(cbStatus, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> {
            if (txtTitle.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tiêu đề!");
                return;
            }

            Post p = post == null ? new Post() : post;
            p.setTitle(txtTitle.getText());
            p.setCategory((String) cbCategory.getSelectedItem());
            p.setContent(txtContent.getText());
            String statusString = (String) cbStatus.getSelectedItem();
            p.setStatus(models.PostStatus.fromDisplayName(statusString));

            boolean success;
            if (post == null) {
                success = controller.createPost(p);
            } else {
                success = controller.updatePost(p);
            }

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Đã lưu thành công!");
                loadData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Lỗi khi lưu!");
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
