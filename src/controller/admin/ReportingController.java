package controller.admin;

import models.Transaction;
import utils.enums.UserRole;
import service.CourseService;
import service.TransactionService;
import service.UserService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ReportingController - Tạo báo cáo và thống kê
 */
public class ReportingController {
    private final TransactionService transactionService;
    private final CourseService courseService;
    private final UserService userService;

    public ReportingController() {
        this.transactionService = new TransactionService();
        this.courseService = new CourseService();
        this.userService = new UserService();
    }

    /**
     * Lấy thống kê tổng quan (số lượng học viên, giảng viên, khóa học...)
     */
    public Map<String, Integer> getSummaryStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("STUDENTS", userService.getUsersByRole(UserRole.HOC_VIEN).size());
        stats.put("TEACHERS", userService.getUsersByRole(UserRole.GIANG_VIEN).size());
        stats.put("STAFF", userService.getUsersByRole(UserRole.NHAN_VIEN).size());
        stats.put("COURSES", courseService.getAllCourses().size());
        return stats;
    }

    /**
     * Lấy doanh thu theo tháng
     */
    public Map<String, BigDecimal> getMonthlyRevenue() {
        return transactionService.getMonthlyRevenue();
    }

    /**
     * Thống kê theo loại giao dịch
     */
    public Map<String, Integer> getTransactionTypeStats() {
        return transactionService.getTransactionTypeStats();
    }

    /**
     * Thống kê đăng ký khóa học
     */
    public Map<String, Integer> getCourseEnrollmentStats() {
        return courseService.getCourseEnrollmentStats();
    }

    /**
     * Lấy 10 hoạt động gần nhất
     */
    public List<Transaction> getRecentActivities() {
        List<Transaction> all = transactionService.getAllTransactions();
        return all.stream()
                .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
