package service;

import models.User;
import models.UserRole;
import repository.IUserRepository;
import repository.impl.UserRepositoryImpl;

import java.util.List;

/**
 * UserService - Xử lý business logic cho User
 */
public class UserService {
    private final IUserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepositoryImpl();
    }

    /**
     * Xác thực user với username và password
     */
    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        return userRepository.authenticate(username, password);
    }

    public List<User> getUsersByRole(UserRole role) {
        return userRepository.getUsersByRole(role);
    }

    public List<User> getUsersByRoleAndPage(UserRole role, int page, int size) {
        return userRepository.getUsersByRoleAndPage(role, page, size);
    }

    public int getTotalUsersByRoleCount(UserRole role) {
        return userRepository.getTotalUsersByRoleCount(role);
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    /**
     * Tạo user mới - kiểm tra username tồn tại
     */
    public boolean createUser(User user) {
        if (userRepository.usernameExists(user.getUsername())) {
            return false;
        }
        return userRepository.createUser(user);
    }

    public boolean updateUser(User user) {
        return userRepository.updateUser(user);
    }

    public boolean deleteUser(int id) {
        return userRepository.deleteUser(id);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public List<User> getUsersByPage(int page, int size) {
        return userRepository.getUsersByPage(page, size);
    }

    public int getTotalUsersCount() {
        return userRepository.getTotalUsersCount();
    }

    public boolean updateStatus(int id, String status) {
        return userRepository.updateUserStatus(id, status);
    }

    public boolean resetPassword(int id, String newPassword) {
        return userRepository.updatePassword(id, newPassword);
    }
}
