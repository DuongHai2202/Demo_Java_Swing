package view.admin.panels;

import controller.admin.TransactionController;
import models.Transaction;
import utils.UIUtils;
import view.admin.dialogs.TransactionDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionManagementPanel extends JPanel {
    private TransactionController transactionController;
    private JTable table;
    private DefaultTableModel model;
    private JFrame parentFrame;
    private models.User currentUser;

    public TransactionManagementPanel(JFrame parentFrame, TransactionController transactionController,
            models.User currentUser) {
        this.parentFrame = parentFrame;
        this.transactionController = transactionController;
        this.currentUser = currentUser;
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        initComponents();
    }

    private void initComponents() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(UIUtils.createHeaderLabel("Giao Dịch"), BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controls.setOpaque(false);
        JTextField tf = new JTextField(15);
        tf.putClientProperty("JTextField.placeholderText", "Tìm kiếm...");
        JButton searchBtn = new JButton("Tìm");
        JButton addBtn = UIUtils.createSuccessButton("+ Lập phiếu");

        controls.add(tf);
        controls.add(searchBtn);
        controls.add(addBtn);
        header.add(controls, BorderLayout.EAST);

        String[] cols = { "ID", "Mã", "Học viên", "Đăng ký ID", "Tiền", "Ngày", "Loại", "Hình thức", "Trạng thái",
                "Người xử lý" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());

        loadTransactions(transactionController.getAllTransactions());

        // Actions panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setOpaque(false);
        JButton editBtn = UIUtils.createPrimaryButton("Sửa");
        JButton deleteBtn = UIUtils.createDangerButton("Xóa");
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);

        searchBtn.addActionListener(e -> {
            loadTransactions(transactionController.searchTransactions(tf.getText()));
        });

        addBtn.addActionListener(e -> {
            TransactionDialog dialog = new TransactionDialog(parentFrame, "Lập phiếu thu/chi mới");
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                models.Transaction t = dialog.getTransaction();
                t.setProcessedBy(currentUser.getId());
                if (transactionController.addTransaction(t)) {
                    UIUtils.showSuccess(parentFrame, "Lập phiếu thành công");
                    loadTransactions(transactionController.getAllTransactions());
                }
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                List<Transaction> list = transactionController.getAllTransactions();
                Transaction selected = list.stream().filter(t -> t.getId() == id).findFirst().orElse(null);

                if (selected != null) {
                    TransactionDialog dialog = new TransactionDialog(parentFrame, "Sửa phiếu giao dịch", selected);
                    dialog.setVisible(true);
                    if (dialog.isConfirmed() && transactionController.updateTransaction(dialog.getTransaction())) {
                        UIUtils.showSuccess(parentFrame, "Cập nhật thành công");
                        loadTransactions(transactionController.getAllTransactions());
                    }
                }
            } else {
                UIUtils.showError(parentFrame, "Vui lòng chọn một hàng để sửa!");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                if (UIUtils.showConfirmation(parentFrame, "Bạn có chắc muốn xóa giao dịch này?")) {
                    int id = (int) model.getValueAt(row, 0);
                    if (transactionController.deleteTransaction(id)) {
                        UIUtils.showSuccess(parentFrame, "Đã xóa thành công");
                        loadTransactions(transactionController.getAllTransactions());
                    }
                }
            } else {
                UIUtils.showError(parentFrame, "Vui lòng chọn một hàng để xóa!");
            }
        });

        add(header, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private void loadTransactions(List<Transaction> list) {
        try {
            model.setRowCount(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            for (Transaction t : list) {
                model.addRow(new Object[] { t.getId(), t.getTransactionCode(), t.getStudentName(),
                        t.getEnrollmentId() != 0 ? t.getEnrollmentId() : "N/A",
                        t.getAmount() != null ? nf.format(t.getAmount()) : "0",
                        t.getTransactionDate() != null ? sdf.format(t.getTransactionDate()) : "",
                        t.getTransactionType(),
                        t.getPaymentMethod(), t.getStatus(),
                        (t.getProcessorName() != null && !t.getProcessorName().isEmpty()) ? t.getProcessorName()
                                : "-" });
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.showError(parentFrame, "Lỗi khi tải danh sách giao dịch: " + e.getMessage());
        }
    }
}
