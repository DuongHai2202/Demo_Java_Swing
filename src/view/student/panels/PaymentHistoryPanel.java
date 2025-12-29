package view.student.panels;

import controller.student.StudentController;
import models.Transaction;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * PaymentHistoryPanel - Lịch sử thanh toán
 */
public class PaymentHistoryPanel extends JPanel {
    private StudentController controller;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JLabel lblTotalPaid;

    public PaymentHistoryPanel(StudentController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 20));
        setBackground(UIUtils.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createSummaryPanel(), BorderLayout.SOUTH);

        loadData();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIUtils.LIGHT_BG);

        JLabel title = new JLabel("Lịch Sử Thanh Toán");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIUtils.PRIMARY_COLOR);

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadData());

        header.add(title, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);

        return header;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        String[] columns = { "Mã GD", "Ngày", "Số tiền", "Loại", "Trạng thái", "Ghi chú" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(tableModel);
        transactionTable.setRowHeight(35);
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Column widths
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        transactionTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        transactionTable.getColumnModel().getColumn(5).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(UIUtils.LIGHT_BG);

        lblTotalPaid = new JLabel("Tổng đã thanh toán: 0 VNĐ");
        lblTotalPaid.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalPaid.setForeground(UIUtils.SUCCESS_COLOR);

        panel.add(lblTotalPaid);

        return panel;
    }

    private void loadData() {
        tableModel.setRowCount(0);

        try {
            List<Transaction> transactions = controller.getMyTransactions();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            long totalPaid = 0;

            for (Transaction txn : transactions) {
                tableModel.addRow(new Object[] {
                        txn.getTransactionCode(),
                        dateFormat.format(txn.getTransactionDate()),
                        currencyFormat.format(txn.getAmount()),
                        txn.getTransactionType(),
                        txn.getStatus(),
                        txn.getDescription() != null ? txn.getDescription() : ""
                });

                if ("Thành công".equals(txn.getStatus())) {
                    totalPaid += txn.getAmount().longValue();
                }
            }

            lblTotalPaid.setText("Tổng đã thanh toán: " + currencyFormat.format(totalPaid));

            if (transactions.isEmpty()) {
                UIUtils.showInfo(this, "Chưa có giao dịch nào");
            }
        } catch (Exception e) {
            UIUtils.showError(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
