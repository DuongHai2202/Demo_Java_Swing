package view.admin.dialogs;

import models.Transaction;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Dialog to add/edit transactions
 */
public class TransactionDialog extends JDialog {
    private JTextField codeField;
    private JTextField studentIdField;
    private JTextField enrollmentIdField;
    private JTextField amountField;
    private JComboBox<String> typeComboBox;
    private JComboBox<String> methodComboBox;
    private JComboBox<String> statusComboBox;
    private JTextArea descriptionArea;

    private boolean confirmed = false;
    private Transaction transaction;
    private boolean isEdit = false;

    public TransactionDialog(Frame owner, String title) {
        super(owner, title, true);
        this.transaction = new Transaction();
        this.isEdit = false;
        initComponents();
    }

    public TransactionDialog(Frame owner, String title, Transaction transaction) {
        super(owner, title, true);
        this.transaction = transaction;
        this.isEdit = true;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setSize(450, 650);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Fields
        codeField = addFormField(mainPanel, "Mã giao dịch (*):", UIUtils.createStyledTextField(0));
        studentIdField = addFormField(mainPanel, "ID Học viên (*):", UIUtils.createStyledTextField(0));
        enrollmentIdField = addFormField(mainPanel, "ID Đăng ký (Enrollment ID):", UIUtils.createStyledTextField(0));
        amountField = addFormField(mainPanel, "Số tiền (*):", UIUtils.createStyledTextField(0));

        mainPanel.add(new JLabel("Loại giao dịch:"));
        typeComboBox = new JComboBox<>(new String[] { "Học phí", "Phí ghi danh", "Giáo trình", "Hoàn tiền", "Khác" });
        typeComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(typeComboBox);
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(new JLabel("Phương thức:"));
        methodComboBox = new JComboBox<>(new String[] { "Tiền mặt", "Chuyển khoản", "Thẻ tín dụng", "Ví điện tử" });
        methodComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(methodComboBox);
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(new JLabel("Trạng thái:"));
        statusComboBox = new JComboBox<>(new String[] { "Thành công", "Đang chờ", "Thất bại", "Đã hoàn tiền" });
        statusComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(statusComboBox);
        mainPanel.add(Box.createVerticalStrut(15));

        mainPanel.add(new JLabel("Mô tả:"));
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane sp = new JScrollPane(descriptionArea);
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        mainPanel.add(sp);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        JButton saveBtn = UIUtils.createSuccessButton("Lưu");
        JButton cancelBtn = UIUtils.createDangerButton("Hủy");

        saveBtn.addActionListener(e -> handleSave());
        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        JScrollPane mainScroll = new JScrollPane(mainPanel);
        mainScroll.setBorder(null);
        add(mainScroll, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JTextField addFormField(JPanel panel, String labelText, JTextField field) {
        panel.add(new JLabel(labelText));
        panel.add(Box.createVerticalStrut(5));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(field);
        panel.add(Box.createVerticalStrut(15));
        return field;
    }

    private void loadData() {
        codeField.setText(transaction.getTransactionCode());
        studentIdField.setText(String.valueOf(transaction.getStudentId()));
        enrollmentIdField
                .setText(transaction.getEnrollmentId() != 0 ? String.valueOf(transaction.getEnrollmentId()) : "");
        amountField.setText(transaction.getAmount().toString());
        typeComboBox.setSelectedItem(transaction.getTransactionType());
        methodComboBox.setSelectedItem(transaction.getPaymentMethod());
        statusComboBox.setSelectedItem(transaction.getStatus());
        descriptionArea.setText(transaction.getDescription());

        if (isEdit) {
            codeField.setEditable(false);
            studentIdField.setEditable(false);
        }
    }

    private void handleSave() {
        try {
            String code = codeField.getText().trim();
            String sId = studentIdField.getText().trim();
            String eId = enrollmentIdField.getText().trim();
            String amt = amountField.getText().trim();

            if (code.isEmpty() || sId.isEmpty() || amt.isEmpty()) {
                UIUtils.showError(this, "Vui lòng điền đầy đủ các trường bắt buộc (*)");
                return;
            }

            transaction.setTransactionCode(code);
            transaction.setStudentId(Integer.parseInt(sId));
            if (!eId.isEmpty()) {
                transaction.setEnrollmentId(Integer.parseInt(eId));
            } else {
                transaction.setEnrollmentId(0);
            }
            transaction.setAmount(new BigDecimal(amt));
            String typeString = (String) typeComboBox.getSelectedItem();
            transaction.setTransactionType(models.TransactionType.fromDisplayName(typeString));
            String methodString = (String) methodComboBox.getSelectedItem();
            transaction.setPaymentMethod(models.PaymentMethod.fromDisplayName(methodString));
            String statusString = (String) statusComboBox.getSelectedItem();
            transaction.setStatus(models.TransactionStatus.fromDisplayName(statusString));
            transaction.setDescription(descriptionArea.getText().trim());

            confirmed = true;
            dispose();
        } catch (NumberFormatException e) {
            UIUtils.showError(this, "ID và Số tiền phải là số hợp lệ!");
        } catch (Exception e) {
            UIUtils.showError(this, "Lỗi: " + e.getMessage());
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
