package repository;

import models.Transaction;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

/**
 * Interface định nghĩa thao tác với bảng tbl_transactions (Giao dịch tài chính)
 */
public interface ITransactionRepository {

    /**
     * Lấy tất cả giao dịch
     * 
     * @return List<Transaction>
     */
    List<Transaction> getAllTransactions();

    /**
     * Tìm kiếm giao dịch
     * 
     * @param keyword Từ khóa (Mã giao dịch hoặc Tên học viên)
     * @return List<Transaction>
     */
    List<Transaction> searchTransactions(String keyword);

    /**
     * Tạo mới giao dịch
     * 
     * @param t Transaction object
     * @return true nếu thành công
     */
    boolean createTransaction(Transaction t);

    /**
     * Cập nhật thông tin giao dịch
     * 
     * @param t Transaction object
     * @return true nếu thành công
     */
    boolean updateTransaction(Transaction t);

    /**
     * Xóa giao dịch
     * 
     * @param id Transaction ID
     * @return true nếu xóa thành công
     */
    boolean deleteTransaction(int id);

    /**
     * Thống kê doanh thu theo tháng (6 tháng gần nhất)
     * 
     * @return Map<Tháng, Tổng tiền>
     */
    Map<String, BigDecimal> getMonthlyRevenue();

    /**
     * Thống kê số lượng giao dịch theo loại
     * 
     * @return Map<Loại giao dịch, Số lượng>
     */
    Map<String, Integer> getTransactionTypeStats();
}
