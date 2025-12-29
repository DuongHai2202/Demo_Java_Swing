package repository.impl;

import database.DatabaseConnection;
import models.Transaction;
import repository.ITransactionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

/**
 * Lớp triển khai cho ITransactionRepository
 */
public class TransactionRepositoryImpl implements ITransactionRepository {

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, u_student.full_name as student_name, u_admin.full_name as processor_name " +
                "FROM tbl_transactions t " +
                "JOIN tbl_students s ON t.student_id = s.id " +
                "JOIN tbl_users u_student ON s.user_id = u_student.id " +
                "LEFT JOIN tbl_users u_admin ON t.processed_by = u_admin.id " +
                "ORDER BY t.transaction_date DESC";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Transaction> searchTransactions(String keyword) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, u_student.full_name as student_name, u_admin.full_name as processor_name " +
                "FROM tbl_transactions t " +
                "JOIN tbl_students s ON t.student_id = s.id " +
                "JOIN tbl_users u_student ON s.user_id = u_student.id " +
                "LEFT JOIN tbl_users u_admin ON t.processed_by = u_admin.id " +
                "WHERE t.transaction_code LIKE ? OR u_student.full_name LIKE ? " +
                "ORDER BY t.transaction_date DESC";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public boolean createTransaction(Transaction t) {
        String sql = "INSERT INTO tbl_transactions (student_id, enrollment_id, transaction_code, amount, " +
                "transaction_type, payment_method, status, description, processed_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, t.getStudentId());
            pstmt.setObject(2, t.getEnrollmentId(), Types.INTEGER);
            pstmt.setString(3, t.getTransactionCode());
            pstmt.setBigDecimal(4, t.getAmount());
            pstmt.setString(5, t.getTransactionType() != null ? t.getTransactionType().getDisplayName() : "Học phí");
            pstmt.setString(6, t.getPaymentMethod() != null ? t.getPaymentMethod().getDisplayName() : "Tiền một");
            pstmt.setString(7, t.getStatus() != null ? t.getStatus().getDisplayName() : "Đang chờ");
            pstmt.setString(8, t.getDescription());
            pstmt.setObject(9, t.getProcessedBy(), Types.INTEGER);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    t.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateTransaction(Transaction t) {
        String sql = "UPDATE tbl_transactions SET amount = ?, transaction_type = ?, payment_method = ?, " +
                "status = ?, description = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setBigDecimal(1, t.getAmount());
            pstmt.setString(2, t.getTransactionType() != null ? t.getTransactionType().getDisplayName() : "Học phí");
            pstmt.setString(3, t.getPaymentMethod() != null ? t.getPaymentMethod().getDisplayName() : "Tiền một");
            pstmt.setString(4, t.getStatus() != null ? t.getStatus().getDisplayName() : "Đang chờ");
            pstmt.setString(5, t.getDescription());
            pstmt.setInt(6, t.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteTransaction(int id) {
        String sql = "DELETE FROM tbl_transactions WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, BigDecimal> getMonthlyRevenue() {
        Map<String, BigDecimal> stats = new LinkedHashMap<>();
        String sql = "SELECT DATE_FORMAT(transaction_date, '%m/%Y') as month, SUM(amount) as total " +
                "FROM tbl_transactions " +
                "WHERE status = 'Thành công' " +
                "GROUP BY month " +
                "ORDER BY MIN(transaction_date) DESC LIMIT 6";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                stats.put(rs.getString("month"), rs.getBigDecimal("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    @Override
    public Map<String, Integer> getTransactionTypeStats() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT transaction_type, COUNT(*) as count FROM tbl_transactions GROUP BY transaction_type";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                stats.put(rs.getString("transaction_type"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getInt("id"));
        transaction.setStudentId(rs.getInt("student_id"));
        transaction.setEnrollmentId(rs.getInt("enrollment_id"));
        transaction.setTransactionCode(rs.getString("transaction_code"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
        transaction.setTransactionType(utils.enums.TransactionType.fromDisplayName(rs.getString("transaction_type")));
        transaction.setPaymentMethod(utils.enums.PaymentMethod.fromDisplayName(rs.getString("payment_method")));
        transaction.setStatus(utils.enums.TransactionStatus.fromDisplayName(rs.getString("status")));
        transaction.setDescription(rs.getString("description"));
        transaction.setProcessedBy(rs.getInt("processed_by"));

        transaction.setStudentName(rs.getString("student_name"));
        transaction.setProcessorName(rs.getString("processor_name"));
        return transaction;
    }
}
