package view.admin.dialogs;

import models.Gender;
import models.User;
import models.UserRole;
import models.UserStatus;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * AccountDialog - Dialog thêm/sửa tài khoản người dùng
 */
public class AccountDialog extends JDialog {
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

        setSize(500, 650);
        setLocationRelativeTo(parent);
    }

    /**
     * Khởi tạo các UI components
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        txtUsername = addField(mainPanel, "Tên đăng nhập:");
        if (isEditMode)
            txtUsername.setEditable(false);

        if (!isEditMode) {
            addLabel(mainPanel, "Mật khẩu:");
            txtPassword = new JPasswordField();
            txtPassword.putClientProperty("FlatLaf.style", "arc: 6");
            mainPanel.add(txtPassword);
            mainPanel.add(Box.createVerticalStrut(10));
        }

        txtFullName = addField(mainPanel, "Họ và tên:");
        txtEmail = addField(mainPanel, "Email:");
        txtPhone = addField(mainPanel, "Số điện thoại:");

        addLabel(mainPanel, "Vai trò:");
        comboRole = new JComboBox<>(new String[] { "Quản trị viên", "Nhân viên", "Giảng viên", "Học viên" });
        mainPanel.add(comboRole);
        mainPanel.add(Box.createVerticalStrut(10));

        addLabel(mainPanel, "Giới tính:");
        comboGender = new JComboBox<>(new String[] { "Nam", "Nữ", "Khác" });
        mainPanel.add(comboGender);
        mainPanel.add(Box.createVerticalStrut(10));

        txtDOB = addField(mainPanel, "Ngày sinh (dd/MM/yyyy):");

        addLabel(mainPanel, "Địa chỉ:");
        areaAddress = new JTextArea(3, 20);
        areaAddress.setLineWrap(true);
        mainPanel.add(new JScrollPane(areaAddress));
        mainPanel.add(Box.createVerticalStrut(10));

        addLabel(mainPanel, "Trạng thái:");
        comboStatus = new JComboBox<>(new String[] { "Đang hoạt động", "Ngừng hoạt động" });
        mainPanel.add(comboStatus);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = UIUtils.createPrimaryButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> handleSave());
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JTextField addField(JPanel panel, String label) {
        addLabel(panel, label);
        JTextField field = UIUtils.createStyledTextField(0);
        panel.add(field);
        panel.add(Box.createVerticalStrut(10));
        return field;
    }

    private void addLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
    }

    /**
     * Load dữ liệu user vào form (khi edit)
     */
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

    /**
     * Xử lý khi click nút Save
     */
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
