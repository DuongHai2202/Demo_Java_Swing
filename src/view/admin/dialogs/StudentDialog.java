package view.admin.dialogs;

import models.Student;
import models.User;
import utils.enums.Gender;
import utils.enums.UserRole;
import utils.enums.UserStatus;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * StudentDialog - Dialog thêm/sửa học viên với UI đẹp
 */
public class StudentDialog extends JDialog {
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
    private JRadioButton rbMale, rbFemale, rbOther;
    private JTextField txtDOB;
    private JTextArea areaAddress;
    private JTextField txtStudentCode;
    private JTextField txtCurrentLevel;
    private JComboBox<String> comboStatus;

    // Data
    private boolean confirmed = false;
    private Student student;
    private boolean isEdit;

    public StudentDialog(JFrame parent, Student student) {
        super(parent, student == null ? "Thêm Học Viên" : "Sửa Học Viên", true);
        this.student = student == null ? new Student() : student;
        if (this.student.getUser() == null) {
            this.student.setUser(new User());
        }
        this.isEdit = student != null;

        initComponents();
        if (isEdit) {
            loadStudentData();
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

        // Student Info Section
        panel.add(createSection("Thông Tin Học Viên", createStudentFields()));
        panel.add(Box.createVerticalStrut(20));

        // Personal Info Section
        panel.add(createSection("Thông Tin Cá Nhân", createPersonalFields()));

        return panel;
    }

    private JPanel createSection(String title, JPanel content) {
        JPanel section = new JPanel(new BorderLayout(0, 12));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, content.getPreferredSize().height + 50));
        section.setBackground(Color.WHITE);

        // Title
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
        if (isEdit)
            txtUsername.setEditable(false);
        panel.add(txtUsername, gbc);

        // Password (only for new)
        if (!isEdit) {
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

        return panel;
    }

    private JPanel createStudentFields() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SECTION_BG);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        // Student Code
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(createLabel("Mã học viên"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtStudentCode = createStyledTextField();
        txtStudentCode.setToolTipText("Ví dụ: S001");
        panel.add(txtStudentCode, gbc);

        // Current Level
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(createLabel("Trình độ hiện tại"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtCurrentLevel = createStyledTextField();
        txtCurrentLevel.setToolTipText("Ví dụ: Beginner, Intermediate, Advanced");
        panel.add(txtCurrentLevel, gbc);

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
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        genderPanel.setOpaque(false);
        rbMale = new JRadioButton("Nam", true);
        rbFemale = new JRadioButton("Nữ");
        rbOther = new JRadioButton("Khác");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbMale);
        bg.add(rbFemale);
        bg.add(rbOther);
        styleRadioButton(rbMale);
        styleRadioButton(rbFemale);
        styleRadioButton(rbOther);
        genderPanel.add(rbMale);
        genderPanel.add(rbFemale);
        genderPanel.add(rbOther);
        panel.add(genderPanel, gbc);

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

    private void styleRadioButton(JRadioButton rb) {
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rb.setOpaque(false);
        rb.setFocusPainted(false);
    }

    private void loadStudentData() {
        User u = student.getUser();
        if (u != null) {
            txtUsername.setText(u.getUsername());
            txtFullName.setText(u.getFullName());
            txtEmail.setText(u.getEmail());
            txtPhone.setText(u.getPhone());

            if (u.getGender() != null) {
                switch (u.getGender()) {
                    case NAM:
                        rbMale.setSelected(true);
                        break;
                    case NU:
                        rbFemale.setSelected(true);
                        break;
                    case KHAC:
                        rbOther.setSelected(true);
                        break;
                }
            }

            if (u.getDateOfBirth() != null) {
                txtDOB.setText(new SimpleDateFormat("dd/MM/yyyy").format(u.getDateOfBirth()));
            }

            areaAddress.setText(u.getAddress());

            if (u.getStatus() != null) {
                comboStatus.setSelectedItem(u.getStatus().getDisplayName());
            }
        }

        txtStudentCode.setText(student.getStudentCode());
        txtCurrentLevel.setText(student.getCurrentLevel());
    }

    private void handleSave() {
        try {
            User user = student.getUser();

            user.setUsername(txtUsername.getText().trim());
            if (!isEdit && txtPassword != null) {
                user.setPassword(new String(txtPassword.getPassword()));
            }

            user.setFullName(txtFullName.getText().trim());
            user.setRole(UserRole.HOC_VIEN);
            user.setEmail(txtEmail.getText().trim());
            user.setPhone(txtPhone.getText().trim());

            // Gender
            if (rbMale.isSelected())
                user.setGender(Gender.NAM);
            else if (rbFemale.isSelected())
                user.setGender(Gender.NU);
            else
                user.setGender(Gender.KHAC);

            // DOB
            String dobStr = txtDOB.getText().trim();
            if (!dobStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                user.setDateOfBirth(new Date(sdf.parse(dobStr).getTime()));
            }

            user.setAddress(areaAddress.getText());

            // Status
            String statusString = (String) comboStatus.getSelectedItem();
            user.setStatus(UserStatus.fromDisplayName(statusString));

            // Student fields
            student.setStudentCode(txtStudentCode.getText().trim());
            student.setCurrentLevel(txtCurrentLevel.getText().trim());

            confirmed = true;
            dispose();
        } catch (Exception e) {
            UIUtils.showError(this, "Lỗi định dạng dữ liệu: " + e.getMessage());
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Student getStudent() {
        return student;
    }
}
