package controller;

import models.User;
import models.UserRole;
import service.UserService;
import utils.UIUtils;
import view.admin.AdminDashboard;
import view.staff.StaffDashboard;

import javax.swing.*;

/**
 * AuthController - Xử lý xác thực và điều hướng người dùng
 */
public class AuthController {
    private final UserService userService;

    public AuthController() {
        this.userService = new UserService();
    }

    /**
     * Xử lý đăng nhập và chuyển đến dashboard tương ứng
     */
    public void login(String username, String password, JFrame loginFrame) {
        if (username.isEmpty() || password.isEmpty()) {
            UIUtils.showError(loginFrame, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        User user = userService.authenticate(username, password);

        if (user == null) {
            UIUtils.showError(loginFrame, "Sai tên đăng nhập hoặc mật khẩu!");
            return;
        }

        loginFrame.dispose();
        navigateToDashboard(user);
    }

    /**
     * Điều hướng đến dashboard dựa trên vai trò
     */
    private void navigateToDashboard(User user) {
        UserRole role = user.getRole();

        if (role == UserRole.QUAN_TRI_VIEN) {
            new AdminDashboard(user).setVisible(true);
        } else if (role == UserRole.NHAN_VIEN || role == UserRole.GIANG_VIEN) {
            new StaffDashboard(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Chức năng dành cho học viên đang phát triển",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Fill tài khoản demo vào form đăng nhập
     */
    public void fillDemoAccount(String role, JTextField usernameField, JPasswordField passwordField) {
        switch (role.toLowerCase()) {
            case "admin":
                usernameField.setText("admin");
                passwordField.setText("123");
                break;
            case "staff":
                usernameField.setText("staff1");
                passwordField.setText("123");
                break;
            case "teacher":
                usernameField.setText("teacher1");
                passwordField.setText("123");
                break;
            case "student":
                usernameField.setText("student1");
                passwordField.setText("123");
                break;
        }
    }
}
