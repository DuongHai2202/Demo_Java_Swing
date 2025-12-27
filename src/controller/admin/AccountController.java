package controller.admin;

import models.User;
import models.UserRole;
import service.UserService;
import java.util.List;

/**
 * AccountController - Quản lý tài khoản người dùng
 */
public class AccountController {
    private final UserService userService;

    public AccountController() {
        this.userService = new UserService();
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Lấy danh sách users theo phân trang
     */
    public List<User> getUsersByPage(int page, int size) {
        return userService.getUsersByPage(page, size);
    }

    public int getTotalUsersCount() {
        return userService.getTotalUsersCount();
    }

    /**
     * Lấy users theo vai trò và phân trang
     */
    public List<User> getUsersByRoleAndPage(UserRole role, int page, int size) {
        return userService.getUsersByRoleAndPage(role, page, size);
    }

    public int getTotalUsersByRoleCount(UserRole role) {
        return userService.getTotalUsersByRoleCount(role);
    }

    /**
     * Thêm tài khoản mới
     */
    public boolean addAccount(User user) {
        return userService.createUser(user);
    }

    /**
     * Cập nhật thông tin tài khoản
     */
    public boolean updateAccount(User user) {
        return userService.updateUser(user);
    }

    /**
     * Xóa tài khoản
     */
    public boolean deleteAccount(int id) {
        return userService.deleteUser(id);
    }
}
