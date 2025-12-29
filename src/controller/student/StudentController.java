package controller.student;

import models.Student;
import models.Transaction;
import models.User;
import service.StudentService;
import service.TransactionService;
import service.UserService;
import utils.enums.TransactionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StudentController - Quản lý nghiệp vụ học viên
 */
public class StudentController {
    private final StudentService studentService;
    private final TransactionService transactionService;
    private final UserService userService;
    private User currentUser;
    private Student currentStudent;

    public StudentController(User user) {
        this.currentUser = user;
        this.studentService = new StudentService();
        this.transactionService = new TransactionService();
        this.userService = new UserService();

        // Load current student info
        this.currentStudent = studentService.getStudentByUserId(user.getId());
    }

    /**
     * Lấy danh sách giao dịch của học viên
     */
    public List<Transaction> getMyTransactions() {
        if (currentStudent == null) {
            return List.of();
        }
        // Get all transactions and filter by student
        return transactionService.getAllTransactions().stream()
                .filter(t -> t.getStudentId() == currentStudent.getId())
                .toList();
    }

    /**
     * Lấy thông tin học viên hiện tại
     */
    public Student getCurrentStudent() {
        return currentStudent;
    }

    /**
     * Lấy thông tin user hiện tại
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Cập nhật thông tin profile
     */
    public boolean updateProfile(User updatedUser) {
        if (userService.updateUser(updatedUser)) {
            this.currentUser = updatedUser;
            return true;
        }
        return false;
    }

    /**
     * Tính tổng số tiền đã thanh toán
     */
    public double getTotalPaid() {
        List<Transaction> transactions = getMyTransactions();
        return transactions.stream()
                .filter(t -> "Thành công".equals(t.getStatus()))
                .mapToDouble(t -> t.getAmount().doubleValue())
                .sum();
    }

    /**
     * Lấy thống kê thanh toán theo loại
     */
    public Map<String, Double> getPaymentStatsByType() {
        Map<String, Double> stats = new HashMap<>();
        List<Transaction> transactions = getMyTransactions();

        for (Transaction t : transactions) {
            if ("Thành công".equals(t.getStatus())) {
                String type = t.getTransactionType().toString();
                double currentAmount = stats.getOrDefault(type, 0.0);
                stats.put(type, currentAmount + t.getAmount().doubleValue());
            }
        }

        return stats;
    }

    /**
     * Đổi mật khẩu
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        // Verify old password first
        User verified = userService.authenticate(currentUser.getUsername(), oldPassword);
        if (verified == null) {
            return false;
        }

        // Update password
        currentUser.setPassword(newPassword);
        return userService.updateUser(currentUser);
    }
}
