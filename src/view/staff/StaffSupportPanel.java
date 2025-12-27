package view.staff;

import controller.staff.StaffController;
import models.SupportRequest;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StaffSupportPanel extends JPanel {
    private StaffController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    public StaffSupportPanel(StaffController controller) {
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

        JLabel title = new JLabel("Chăm Sóc Khách Hàng");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIUtils.PRIMARY_COLOR);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controls.setBackground(UIUtils.LIGHT_BG);

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadData());
        controls.add(btnRefresh);

        header.add(title, BorderLayout.WEST);
        header.add(controls, BorderLayout.EAST);
        return header;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = { "ID", "Người yêu cầu", "Email", "SĐT", "Chủ đề", "Trạng thái", "Ngày tạo" };
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

        // Sorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Click to view details
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    showDetailDialog();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<SupportRequest> list = controller.getAllSupportRequests();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (SupportRequest req : list) {
            Object[] row = {
                    req.getId(),
                    req.getRequesterName() != null ? req.getRequesterName() : "N/A",
                    req.getRequesterEmail(),
                    req.getRequesterPhone(),
                    req.getSubject(),
                    req.getStatus(),
                    req.getCreatedAt() != null ? req.getCreatedAt().format(formatter) : ""
            };
            tableModel.addRow(row);
        }
    }

    private void showDetailDialog() {
        int row = table.getSelectedRow();
        int id = (int) table.getValueAt(row, 0);

        SupportRequest selected = null;
        for (SupportRequest req : controller.getAllSupportRequests()) {
            if (req.getId() == id) {
                selected = req;
                break;
            }
        }

        if (selected == null)
            return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi Tiết Yêu Cầu", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;

        addDetailRow(form, gbc, "Người gửi:", selected.getRequesterName());
        addDetailRow(form, gbc, "Email:", selected.getRequesterEmail());
        addDetailRow(form, gbc, "SĐT:", selected.getRequesterPhone());
        addDetailRow(form, gbc, "Chủ đề:", selected.getSubject());

        gbc.gridx = 0;
        gbc.gridy++;
        form.add(new JLabel("Nội dung:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextArea txtMessage = new JTextArea(selected.getMessage());
        txtMessage.setRows(5);
        txtMessage.setLineWrap(true);
        txtMessage.setWrapStyleWord(true);
        txtMessage.setEditable(false);
        form.add(new JScrollPane(txtMessage), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        form.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        String[] statuses = { "Mới", "Đang xử lý", "Đã giải quyết", "Đã đóng" };
        JComboBox<String> cbStatus = new JComboBox<>(statuses);
        cbStatus.setSelectedItem(selected.getStatus());
        form.add(cbStatus, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Cập Nhật Trạng Thái");
        JButton btnClose = new JButton("Đóng");

        SupportRequest finalSelected = selected;
        btnSave.addActionListener(e -> {
            boolean success = controller.updateSupportStatus(finalSelected.getId(),
                    (String) cbStatus.getSelectedItem());
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Đã cập nhật!");
                loadData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Lỗi cập nhật!");
            }
        });

        btnClose.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnClose);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, String label, String value) {
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        JTextField txt = new JTextField(value);
        txt.setEditable(false);
        panel.add(txt, gbc);

        gbc.gridy++;
    }
}
