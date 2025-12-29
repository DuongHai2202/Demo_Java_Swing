package view.admin.dialogs;

import utils.enums.Gender;
import models.User;
import utils.enums.UserRole;
import utils.enums.UserStatus;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * AccountDialog - Dialog thêm/sửa tài khoản với UI đẹp
 */
public class AccountDialog extends JDialog {
    // Constants
    private static final int DIALOG_WIDTH = 600;
    private static final int DIALOG_HEIGHT = 700;
    private static final int FIELD_HEIGHT = 35;
    private static final Color SECTION_BG = new Color(249, 250, 251);

    // UI Components
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JComboBox<String> comboRole;
    private JComboBox<String> comboGender;
    private JComboBox<String> comboStatus;
    private JTextField txtDOB;
    private JTextArea areaAddress;

    // Data
    private boolean confirmed = false;
    private User user;
    private boolean isEditMode;

    public AccountDialog(JFrame parent, User user) {
        super(parent, user == null ? "Thêm Tài Khoản" : "Sửa Tài Khoản", true);
        this.user = user == null ? new User() : user;
        this.isEditMode = user != null;

        initComponents();
        if (isEditMode) {
            loadUserData();
        }

        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Color.WHITE);

        // Main content with scroll
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

        // Account Section
        panel.add(createSection("Thông Tin Tài Khoản", createAccountFields()));
        panel.add(Box.createVerticalStrut(20));

        // Personal Info Section
        panel.add(createSection("Thông Tin Cá Nhân", createPersonalFields()));

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

    private JPanel createAccountFields() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SECTION_BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        // Username
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(createLabel("Tên đăng nhập"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtUsername = createStyledTextField();
        if (isEditMode)
            txtUsername.setEditable(false);
        panel.add(txtUsername, gbc);

        // Password
        if (!isEditMode) {
            row++;
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0.3;
            panel.add(createLabel("Mật khẩu"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            txtPassword = new JPasswordField();
            styleField(txtPassword);
            panel.add(txtPassword, gbc);
        }

        // Full Name
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(createLabel("Họ và tên"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtFullName = createStyledTextField();
        panel.add(txtFullName, gbc);

        // Role
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(createLabel("Vai trò"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        comboRole = new JComboBox<>(new String[] { "Quản trị viên", "Nhân viên", "Giảng viên", "Học viên" });
        styleField(comboRole);
        panel.add(comboRole, gbc);

        // Status
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(createLabel("Trạng thái"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        comboStatus = new JComboBox<>(new String[] { "Đang hoạt động", "Ngừng hoạt động" });
        styleField(comboStatus);
        panel.add(comboStatus, gbc);

        return panel;
    }

    private JPanel createPersonalFields() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SECTION_BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        // Email
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(createLabel("Email"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtEmail = createStyledTextField();
        panel.add(txtEmail, gbc);

        // Phone
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(createLabel("Số điện thoại"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtPhone = createStyledTextField();
        panel.add(txtPhone, gbc);

        // Gender
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(createLabel("Giới tính"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        comboGender = new JComboBox<>(new String[] { "Nam", "Nữ", "Khác" });
        styleField(comboGender);
        panel.add(comboGender, gbc);

        // Date of Birth
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(createLabel("Ngày sinh"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtDOB = createStyledTextField();
        txtDOB.setToolTipText("dd/MM/yyyy");
        panel.add(txtDOB, gbc);

        // Address
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(createLabel("Địa chỉ"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.CENTER;
        areaAddress = new JTextArea(3, 20);
        areaAddress.setLineWrap(true);
        areaAddress.setWrapStyleWord(true);
        areaAddress.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollAddress = new JScrollPane(areaAddress);
        scrollAddress.setPreferredSize(new Dimension(0, 70));
        styleField(scrollAddress);
        panel.add(scrollAddress, gbc);

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

    private void loadUserData() {
        txtUsername.setText(user.getUsername());
        txtFullName.setText(user.getFullName());
        txtEmail.setText(user.getEmail());
        txtPhone.setText(user.getPhone());

        if (user.getRole() != null) {
            comboRole.setSelectedItem(user.getRole().getDisplayName());
        }

        if (user.getGender() != null) {
            comboGender.setSelectedItem(user.getGender().getDisplayName());
        }

        if (user.getDateOfBirth() != null) {
            txtDOB.setText(new SimpleDateFormat("dd/MM/yyyy").format(user.getDateOfBirth()));
        }

        areaAddress.setText(user.getAddress());

        if (user.getStatus() != null) {
            comboStatus.setSelectedItem(user.getStatus().getDisplayName());
        }
    }

    private void handleSave() {
        try {
            user.setUsername(txtUsername.getText().trim());
            if (!isEditMode) {
                user.setPassword(new String(txtPassword.getPassword()));
            }

            user.setFullName(txtFullName.getText().trim());
            user.setEmail(txtEmail.getText().trim());
            user.setPhone(txtPhone.getText().trim());

            String roleStr = (String) comboRole.getSelectedItem();
            user.setRole(UserRole.fromDisplayName(roleStr));

            String genderStr = (String) comboGender.getSelectedItem();
            user.setGender(Gender.fromDisplayName(genderStr));

            String dobStr = txtDOB.getText().trim();
            if (!dobStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                user.setDateOfBirth(new Date(sdf.parse(dobStr).getTime()));
            }

            user.setAddress(areaAddress.getText());

            String statusStr = (String) comboStatus.getSelectedItem();
            user.setStatus(UserStatus.fromDisplayName(statusStr));

            confirmed = true;
            dispose();
        } catch (Exception e) {
            UIUtils.showError(this, "Lỗi định dạng dữ liệu: " + e.getMessage());
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public User getUser() {
        return user;
    }
}
