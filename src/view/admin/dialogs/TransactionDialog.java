package view.admin.dialogs;

import models.Transaction;
import utils.enums.PaymentMethod;
import utils.enums.TransactionStatus;
import utils.enums.TransactionType;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;

/**
 * TransactionDialog - Dialog thêm/sửa giao dịch với UI đẹp
 */
public class TransactionDialog extends JDialog {
    // Constants
    private static final int DIALOG_WIDTH = 550;
    private static final int DIALOG_HEIGHT = 600;
    private static final int FIELD_HEIGHT = 35;
    private static final Color SECTION_BG = new Color(249, 250, 251);

    // UI Components
    private JTextField codeField;
    private JTextField studentIdField;
    private JTextField enrollmentIdField;
    private JTextField amountField;
    private JComboBox<String> typeComboBox;
    private JComboBox<String> methodComboBox;
    private JComboBox<String> statusComboBox;
    private JTextArea descriptionArea;

    // Data
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
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Color.WHITE);

        // Main content
        JPanel mainPanel = createMainPanel();
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Transaction Info Section
        panel.add(createSection("Thông Tin Giao Dịch", createTransactionFields()));
        panel.add(Box.createVerticalStrut(20));

        // Payment Section
        panel.add(createSection("Thanh Toán", createPaymentFields()));

        return panel;
    }

    private JPanel createSection(String title, JPanel content) {
        JPanel section = new JPanel(new BorderLayout(0, 12));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, content.getPreferredSize().height + 50));
        section.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(51, 51, 51));

        section.add(lblTitle, BorderLayout.NORTH);
        section.add(content, BorderLayout.CENTER);

        return section;
    }

    private JPanel createTransactionFields() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SECTION_BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        // Transaction Code
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        panel.add(createLabel("Mã giao dịch"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        codeField = createStyledTextField();
        panel.add(codeField, gbc);

        // Student ID
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        panel.add(createLabel("ID Học viên"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        studentIdField = createStyledTextField();
        panel.add(studentIdField, gbc);

        // Enrollment ID
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        panel.add(createLabel("ID Đăng ký"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        enrollmentIdField = createStyledTextField();
        enrollmentIdField.setToolTipText("Optional");
        panel.add(enrollmentIdField, gbc);

        // Description
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(createLabel("Mô tả"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.anchor = GridBagConstraints.CENTER;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollDesc = new JScrollPane(descriptionArea);
        scrollDesc.setPreferredSize(new Dimension(0, 70));
        styleField(scrollDesc);
        panel.add(scrollDesc, gbc);

        return panel;
    }

    private JPanel createPaymentFields() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SECTION_BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        // Amount
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        panel.add(createLabel("Số tiền"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        amountField = createStyledTextField();
        amountField.setToolTipText("VNĐ");
        panel.add(amountField, gbc);

        // Transaction Type
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        panel.add(createLabel("Loại giao dịch"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        typeComboBox = new JComboBox<>(new String[] { "Học phí", "Phí ghi danh", "Giáo trình", "Hoàn tiền", "Khác" });
        styleField(typeComboBox);
        panel.add(typeComboBox, gbc);

        // Payment Method
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        panel.add(createLabel("Phương thức"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        methodComboBox = new JComboBox<>(new String[] { "Tiền mặt", "Chuyển khoản", "Thẻ tín dụng", "Ví điện tử" });
        styleField(methodComboBox);
        panel.add(methodComboBox, gbc);

        // Status
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        panel.add(createLabel("Trạng thái"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        statusComboBox = new JComboBox<>(new String[] { "Thành công", "Đang chờ", "Thất bại", "Đã hoàn tiền" });
        styleField(statusComboBox);
        panel.add(statusComboBox, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        JButton btnSave = UIUtils.createPrimaryButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSave.setPreferredSize(new Dimension(100, 38));
        btnCancel.setPreferredSize(new Dimension(100, 38));

        btnSave.addActionListener(e -> handleSave());
        btnCancel.addActionListener(e -> dispose());

        panel.add(btnCancel);
        panel.add(btnSave);

        return panel;
    }

    // Helper methods
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        styleField(field);
        return field;
    }

    private void styleField(JComponent field) {
        field.setPreferredSize(new Dimension(0, FIELD_HEIGHT));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.putClientProperty("FlatLaf.style", "arc: 8");
    }

    private void loadData() {
        if (transaction != null) {
            codeField.setText(transaction.getTransactionCode());
            studentIdField.setText(String.valueOf(transaction.getStudentId()));
            enrollmentIdField.setText(String.valueOf(transaction.getEnrollmentId()));
            amountField.setText(transaction.getAmount() != null ? transaction.getAmount().toString() : "");
            descriptionArea.setText(transaction.getDescription());

            if (transaction.getTransactionType() != null) {
                typeComboBox.setSelectedItem(transaction.getTransactionType().getDisplayName());
            }
            if (transaction.getPaymentMethod() != null) {
                methodComboBox.setSelectedItem(transaction.getPaymentMethod().getDisplayName());
            }
            if (transaction.getStatus() != null) {
                statusComboBox.setSelectedItem(transaction.getStatus().getDisplayName());
            }
        }
    }

    private void handleSave() {
        try {
            transaction.setTransactionCode(codeField.getText().trim());
            transaction.setStudentId(Integer.parseInt(studentIdField.getText().trim()));

            String enrollmentText = enrollmentIdField.getText().trim();
            if (!enrollmentText.isEmpty()) {
                transaction.setEnrollmentId(Integer.parseInt(enrollmentText));
            }

            transaction.setAmount(new BigDecimal(amountField.getText().trim()));
            transaction.setDescription(descriptionArea.getText());

            String typeString = (String) typeComboBox.getSelectedItem();
            transaction.setTransactionType(TransactionType.fromDisplayName(typeString));

            String methodString = (String) methodComboBox.getSelectedItem();
            transaction.setPaymentMethod(PaymentMethod.fromDisplayName(methodString));

            String statusString = (String) statusComboBox.getSelectedItem();
            transaction.setStatus(TransactionStatus.fromDisplayName(statusString));

            confirmed = true;
            dispose();
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
