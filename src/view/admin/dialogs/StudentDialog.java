package view.admin.dialogs;

import models.Student;
import models.User;
import models.UserRole;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class StudentDialog extends JDialog {
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

        setSize(500, 650);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Account Fields
        txtUsername = addField(mainPanel, "Tên đăng nhập:", false);
        if (isEdit)
            txtUsername.setEditable(false);

        if (!isEdit) {
            addLabel(mainPanel, "Mật khẩu:");
            txtPassword = new JPasswordField();
            txtPassword.putClientProperty("FlatLaf.style", "arc: 6");
            mainPanel.add(txtPassword);
            mainPanel.add(Box.createVerticalStrut(10));
        }

        txtFullName = addField(mainPanel, "Họ và tên:", false);

        // Student Specific Fields
        txtStudentCode = addField(mainPanel, "Mã học viên (ví dụ: S001):", false);
        txtCurrentLevel = addField(mainPanel, "Trình độ hiện tại (ví dụ: Beginner):", false);

        txtEmail = addField(mainPanel, "Email:", false);
        txtPhone = addField(mainPanel, "Số điện thoại:", false);

        addLabel(mainPanel, "Giới tính:");
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setOpaque(false);
        rbMale = new JRadioButton("Nam", true);
        rbFemale = new JRadioButton("Nữ");
        rbOther = new JRadioButton("Khác");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbMale);
        bg.add(rbFemale);
        bg.add(rbOther);
        genderPanel.add(rbMale);
        genderPanel.add(rbFemale);
        genderPanel.add(rbOther);
        mainPanel.add(genderPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        txtDOB = addField(mainPanel, "Ngày sinh (dd/MM/yyyy):", false);

        addLabel(mainPanel, "Địa chỉ:");
        areaAddress = new JTextArea(3, 20);
        areaAddress.setLineWrap(true);
        areaAddress.setWrapStyleWord(true);
        areaAddress.putClientProperty("JComponent.roundRect", false);
        mainPanel.add(new JScrollPane(areaAddress));
        mainPanel.add(Box.createVerticalStrut(10));

        addLabel(mainPanel, "Trạng thái:");
        comboStatus = new JComboBox<>(new String[] { "Đang hoạt động", "Ngừng hoạt động" });
        mainPanel.add(comboStatus);
        mainPanel.add(Box.createVerticalStrut(20));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = UIUtils.createPrimaryButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> handleSave());
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JTextField addField(JPanel panel, String label, boolean isPass) {
        addLabel(panel, label);
        JTextField field = UIUtils.createStyledTextField(0);
        panel.add(field);
        panel.add(Box.createVerticalStrut(10));
        return field;
    }

    private void addLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
    }

    private void loadStudentData() {
        User user = student.getUser();
        txtUsername.setText(user.getUsername());
        txtFullName.setText(user.getFullName());
        txtStudentCode.setText(student.getStudentCode());
        txtCurrentLevel.setText(student.getCurrentLevel());
        txtEmail.setText(user.getEmail());
        txtPhone.setText(user.getPhone());

        if (user.getGender() != null) {
            String genderDisplay = user.getGender().getDisplayName();
            if ("Nam".equalsIgnoreCase(genderDisplay))
                rbMale.setSelected(true);
            else if ("Nữ".equalsIgnoreCase(genderDisplay))
                rbFemale.setSelected(true);
            else
                rbOther.setSelected(true);
        }

        if (user.getDateOfBirth() != null) {
            txtDOB.setText(new SimpleDateFormat("dd/MM/yyyy").format(user.getDateOfBirth()));
        }
        areaAddress.setText(user.getAddress());
        comboStatus.setSelectedItem(user.getStatus());
    }

    private void handleSave() {
        try {
            User user = student.getUser();
            user.setUsername(txtUsername.getText().trim());
            if (!isEdit)
                user.setPassword(new String(txtPassword.getPassword()));
            user.setRole(UserRole.HOC_VIEN);
            user.setFullName(txtFullName.getText().trim());
            user.setEmail(txtEmail.getText().trim());
            user.setPhone(txtPhone.getText().trim());

            String genderString = rbMale.isSelected() ? "Nam" : (rbFemale.isSelected() ? "Nữ" : "Khác");
            user.setGender(models.Gender.fromDisplayName(genderString));

            String dobStr = txtDOB.getText().trim();
            if (!dobStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                user.setDateOfBirth(new java.sql.Date(sdf.parse(dobStr).getTime()));
            }
            String statusString = (String) comboStatus.getSelectedItem();
            user.setStatus(models.UserStatus.fromDisplayName(statusString));

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
