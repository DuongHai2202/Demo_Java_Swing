package view.admin.dialogs;

import models.Staff;
import models.User;
import utils.enums.UserRole;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Dialog để thêm mới nhân viên
 */
public class AddStaffDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JRadioButton maleBtn;
    private JRadioButton femaleBtn;
    private JRadioButton otherBtn;
    private ButtonGroup genderGroup;
    private JTextField dobField;
    private JTextArea addressArea;
    private JComboBox<String> statusComboBox;

    // Staff specific fields
    private JTextField staffCodeField;
    private JTextField positionField;
    private JTextField departmentField;
    private JTextField hireDateField;

    private boolean confirmed = false;
    private Staff newStaff;

    public AddStaffDialog(Frame owner) {
        super(owner, "Thêm nhân viên mới", true);
        initComponents();
    }

    private void initComponents() {
        setSize(550, 650);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = UIUtils.createHeaderLabel("THÔNG TIN NHÂN VIÊN MỚI");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // User account fields
        usernameField = addFormField(mainPanel, "Tên đăng nhập (*):", UIUtils.createStyledTextField(0));
        passwordField = (JPasswordField) addFormField(mainPanel, "Mật khẩu (*):", UIUtils.createStyledPasswordField(0));
        fullNameField = addFormField(mainPanel, "Họ và tên (*):", UIUtils.createStyledTextField(0));

        // Staff specific fields
        staffCodeField = addFormField(mainPanel, "Mã nhân viên (Staff Code):", UIUtils.createStyledTextField(0));
        positionField = addFormField(mainPanel, "Chức vụ (Position):", UIUtils.createStyledTextField(0));
        departmentField = addFormField(mainPanel, "Phòng ban (Department):", UIUtils.createStyledTextField(0));
        hireDateField = addFormField(mainPanel, "Ngày vào làm (yyyy-mm-dd):", UIUtils.createStyledTextField(0));

        emailField = addFormField(mainPanel, "Email (*):", UIUtils.createStyledTextField(0));
        phoneField = addFormField(mainPanel, "Số điện thoại:", UIUtils.createStyledTextField(0));

        // Gender
        mainPanel.add(createLabel("Giới tính:"));
        mainPanel.add(Box.createVerticalStrut(5));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.setOpaque(false);
        genderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        maleBtn = new JRadioButton("Nam", true);
        femaleBtn = new JRadioButton("Nữ");
        otherBtn = new JRadioButton("Khác");
        maleBtn.setBackground(Color.WHITE);
        femaleBtn.setBackground(Color.WHITE);
        otherBtn.setBackground(Color.WHITE);

        genderGroup = new ButtonGroup();
        genderGroup.add(maleBtn);
        genderGroup.add(femaleBtn);
        genderGroup.add(otherBtn);

        genderPanel.add(maleBtn);
        genderPanel.add(Box.createHorizontalStrut(20));
        genderPanel.add(femaleBtn);
        genderPanel.add(Box.createHorizontalStrut(20));
        genderPanel.add(otherBtn);

        mainPanel.add(genderPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Date of Birth
        dobField = addFormField(mainPanel, "Ngày sinh (dd/MM/yyyy):", UIUtils.createStyledTextField(0));

        // Address
        mainPanel.add(createLabel("Địa chỉ:"));
        mainPanel.add(Box.createVerticalStrut(5));
        addressArea = new JTextArea(3, 30);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(addressScroll);
        mainPanel.add(Box.createVerticalStrut(15));

        // Status
        mainPanel.add(createLabel("Trạng thái:"));
        mainPanel.add(Box.createVerticalStrut(5));
        statusComboBox = new JComboBox<>(new String[] { "Đang hoạt động", "Ngừng hoạt động" });
        statusComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        mainPanel.add(statusComboBox);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton saveButton = UIUtils.createSuccessButton("Lưu");
        saveButton.addActionListener(e -> handleSave());

        JButton cancelButton = UIUtils.createDangerButton("Hủy");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        JScrollPane containerScroll = new JScrollPane(mainPanel);
        containerScroll.setBorder(null);
        add(containerScroll, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JTextField addFormField(JPanel panel, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(field);
        panel.add(Box.createVerticalStrut(15));
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void handleSave() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();
        String staffCode = staffCodeField.getText().trim();
        String position = positionField.getText().trim();
        String department = departmentField.getText().trim();
        String hireDateStr = hireDateField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            UIUtils.showError(this, "Vui lòng điền đầy đủ thông tin bắt buộc!");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone.isEmpty() ? null : phone);
        user.setAddress(address.isEmpty() ? null : address);
        user.setRole(UserRole.NHAN_VIEN);
        String genderString = maleBtn.isSelected() ? "Nam" : (femaleBtn.isSelected() ? "Nữ" : "Khác");
        user.setGender(utils.enums.Gender.fromDisplayName(genderString));

        String statusString = (String) statusComboBox.getSelectedItem();
        user.setStatus(utils.enums.UserStatus.fromDisplayName(statusString));

        String dobStr = dobField.getText().trim();
        if (!dobStr.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date parsedDate = sdf.parse(dobStr);
                user.setDateOfBirth(new java.sql.Date(parsedDate.getTime()));
            } catch (Exception e) {
            }
        }

        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        newStaff = new Staff();
        newStaff.setUser(user);
        newStaff.setStaffCode(staffCode.isEmpty() ? null : staffCode);
        newStaff.setPosition(position.isEmpty() ? null : position);
        newStaff.setDepartment(department.isEmpty() ? null : department);

        if (!hireDateStr.isEmpty()) {
            try {
                newStaff.setHireDate(Date.valueOf(hireDateStr));
            } catch (Exception e) {
            }
        }

        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Staff getNewStaff() {
        return newStaff;
    }
}
