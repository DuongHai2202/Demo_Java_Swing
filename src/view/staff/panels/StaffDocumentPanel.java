package view.staff.panels;

import controller.staff.StaffController;
import models.Document;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * StaffDocumentPanel - Quản lý tài liệu
 */
public class StaffDocumentPanel extends JPanel {
    private StaffController controller;
    private JTable documentTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> comboCategory;

    public StaffDocumentPanel(StaffController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 20));
        setBackground(UIUtils.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        loadMockData();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIUtils.LIGHT_BG);

        JLabel title = new JLabel("Quản Lý Tài Liệu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIUtils.PRIMARY_COLOR);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(UIUtils.LIGHT_BG);

        JLabel lblCategory = new JLabel("Danh mục:");
        comboCategory = new JComboBox<>(new String[] { "Tất cả", "Giáo trình", "Bài tập", "Tài liệu tham khảo" });
        comboCategory.setPreferredSize(new Dimension(180, 35));
        comboCategory.addActionListener(e -> filterByCategory());

        JButton btnUpload = UIUtils.createPrimaryButton("+ Tải lên");
        btnUpload.setPreferredSize(new Dimension(120, 35));
        btnUpload.addActionListener(e -> handleUpload());

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setPreferredSize(new Dimension(100, 35));
        btnRefresh.addActionListener(e -> loadMockData());

        rightPanel.add(lblCategory);
        rightPanel.add(comboCategory);
        rightPanel.add(btnUpload);
        rightPanel.add(btnRefresh);

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        String[] columns = { "Tên tài liệu", "Loại file", "Kích thước", "Danh mục", "Ngày tải" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        documentTable = new JTable(tableModel);
        documentTable.setRowHeight(35);
        documentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        documentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        documentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Column widths
        documentTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        documentTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        documentTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        documentTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        documentTable.getColumnModel().getColumn(4).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(documentTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(UIUtils.LIGHT_BG);

        JButton btnView = new JButton("Xem");
        btnView.setPreferredSize(new Dimension(100, 40));
        btnView.addActionListener(e -> handleView());

        JButton btnDownload = UIUtils.createPrimaryButton("Tải xuống");
        btnDownload.setPreferredSize(new Dimension(120, 40));
        btnDownload.addActionListener(e -> handleDownload());

        panel.add(btnView);
        panel.add(btnDownload);

        return panel;
    }

    private void loadMockData() {
        tableModel.setRowCount(0);

        // Mock data
        List<Object[]> documents = new ArrayList<>();
        documents.add(new Object[] { "Bài học 1 - Giới thiệu", "PDF", "2.5 MB", "Giáo trình", "20/12/2024" });
        documents.add(new Object[] { "Đề thi giữa kỳ", "DOCX", "1.2 MB", "Bài tập", "15/12/2024" });
        documents.add(new Object[] { "Ngữ pháp nâng cao", "PDF", "3.8 MB", "Tài liệu tham khảo", "10/12/2024" });
        documents.add(new Object[] { "Bài tập Unit 5", "PDF", "850 KB", "Bài tập", "05/12/2024" });

        for (Object[] row : documents) {
            tableModel.addRow(row);
        }
    }

    private void filterByCategory() {
        String category = (String) comboCategory.getSelectedItem();
        // In real impl, filter data by category
        UIUtils.showInfo(this, "Lọc theo: " + category);
    }

    private void handleUpload() {
        UIUtils.showInfo(this, "Chức năng tải lên tài liệu đang phát triển!");
    }

    private void handleView() {
        int selectedRow = documentTable.getSelectedRow();
        if (selectedRow == -1) {
            UIUtils.showWarning(this, "Vui lòng chọn tài liệu để xem!");
            return;
        }

        String docName = (String) tableModel.getValueAt(selectedRow, 0);
        UIUtils.showInfo(this, "Xem tài liệu: " + docName);
    }

    private void handleDownload() {
        int selectedRow = documentTable.getSelectedRow();
        if (selectedRow == -1) {
            UIUtils.showWarning(this, "Vui lòng chọn tài liệu để tải!");
            return;
        }

        String docName = (String) tableModel.getValueAt(selectedRow, 0);
        UIUtils.showInfo(this, "Tải xuống: " + docName);
    }
}
