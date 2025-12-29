package view.student.panels;

import controller.student.StudentController;
import models.Student;
import models.User;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * ProfilePanel - Thông tin cá nhân
 */
public class ProfilePanel extends JPanel {
    private StudentController controller;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;

    public ProfilePanel(StudentController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 20));
        setBackground(UIUtils.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(createHeader(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadData();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIUtils.LIGHT_BG);

        JLabel title = new JLabel("Thông Tin Cá Nhân");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIUtils.PRIMARY_COLOR);

        header.add(title, BorderLayout.WEST);

        return header;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        User user = controller.getCurrentUser();
        Student student = controller.getCurrentStudent();

        int row = 0;

        // Mã học viên (read-only)
        addFormRow(panel, gbc, row++, "Mã học viên:",
                new JLabel(student != null ? student.getStudentCode() : "N/A"));

        // Họ tên (read-only)
        addFormRow(panel, gbc, row++, "Họ và tên:",
                new JLabel(user.getFullName()));

        // Username (read-only)
        addFormRow(panel, gbc, row++, "Tên đăng nhập:",
                new JLabel(user.getUsername()));

        // Email (editable)
        txtEmail = new JTextField(user.getEmail(), 25);
        addFormRow(panel, gbc, row++, "Email:", txtEmail);

        // Phone (editable)
        txtPhone = new JTextField(user.getPhone(), 25);
        addFormRow(panel, gbc, row++, "Số điện thoại:", txtPhone);

        // Address (editable)
        txtAddress = new JTextField(user.getAddress(), 25);
        addFormRow(panel, gbc, row++, "Địa chỉ:", txtAddress);

        // Gender (read-only)
        addFormRow(panel, gbc, row++, "Giới tính:",
                new JLabel(user.getGender() != null ? user.getGender().toString() : "N/A"));

        // Status (read-only)
        addFormRow(panel, gbc, row++, "Trạng thái:",
                new JLabel(user.getStatus() != null ? user.getStatus().toString() : "N/A"));

        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        if (component instanceof JLabel) {
            ((JLabel) component).setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }
        panel.add(component, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(UIUtils.LIGHT_BG);

        JButton btnChangePassword = new JButton("Đổi mật khẩu");
        btnChangePassword.setPreferredSize(new Dimension(140, 40));
        btnChangePassword.addActionListener(e -> handleChangePassword());

        JButton btnSave = UIUtils.createPrimaryButton("Lưu thay đổi");
        btnSave.setPreferredSize(new Dimension(140, 40));
        btnSave.addActionListener(e -> handleSave());

        panel.add(btnChangePassword);
        panel.add(btnSave);

        return panel;
    }

    private void loadData() {
        User user = controller.getCurrentUser();
        txtEmail.setText(user.getEmail());
        txtPhone.setText(user.getPhone());
        txtAddress.setText(user.getAddress());
    }

    private void handleSave() {
        User user = controller.getCurrentUser();
        user.setEmail(txtEmail.getText().trim());
        user.setPhone(txtPhone.getText().trim());
        user.setAddress(txtAddress.getText().trim());

        if (controller.updateProfile(user)) {
            UIUtils.showSuccess(this, "Cập nhật thông tin thành công!");
        } else {
            UIUtils.showError(this, "Lỗi khi cập nhật thông tin!");
        }
    }

    private void handleChangePassword() {
        JPasswordField oldPasswordField = new JPasswordField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Mật khẩu cũ:"));
        panel.add(oldPasswordField);
        panel.add(new JLabel("Mật khẩu mới:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Xác nhận:"));
        panel.add(confirmPasswordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Đổi mật khẩu",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (newPassword.isEmpty()) {
                UIUtils.showError(this, "Mật khẩu mới không được để trống!");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                UIUtils.showError(this, "Mật khẩu xác nhận không khớp!");
                return;
            }

            if (controller.changePassword(oldPassword, newPassword)) {
                UIUtils.showSuccess(this, "Đổi mật khẩu thành công!");
            } else {
                UIUtils.showError(this, "Mật khẩu cũ không đúng!");
            }
        }
    }
}
