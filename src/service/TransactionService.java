package service;

import models.Transaction;
import repository.ITransactionRepository;
import repository.impl.TransactionRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * TransactionService - Xử lý business logic cho Transaction
 */
public class TransactionService {
    private final ITransactionRepository transactionRepository;

    public TransactionService() {
        this.transactionRepository = new TransactionRepositoryImpl();
    }

    /**
     * Lấy thống kê doanh thu theo tháng
     */
    public Map<String, BigDecimal> getMonthlyRevenue() {
        return transactionRepository.getMonthlyRevenue();
    }

    /**
     * Thống kê theo loại giao dịch
     */
    public Map<String, Integer> getTransactionTypeStats() {
        return transactionRepository.getTransactionTypeStats();
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.getAllTransactions();
    }

    /**
     * Tìm kiếm giao dịch theo từ khóa
     */
    public List<Transaction> searchTransactions(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return transactionRepository.getAllTransactions();
        }
        return transactionRepository.searchTransactions(keyword);
    }

    public boolean createTransaction(Transaction t) {
        return transactionRepository.createTransaction(t);
    }

    public boolean updateTransaction(Transaction t) {
        return transactionRepository.updateTransaction(t);
    }

    public boolean deleteTransaction(int id) {
        return transactionRepository.deleteTransaction(id);
    }
}
