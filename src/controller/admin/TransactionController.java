package controller.admin;

import models.Transaction;
import service.TransactionService;
import java.util.List;

/**
 * TransactionController - Quản lý giao dịch
 */
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController() {
        this.transactionService = new TransactionService();
    }

    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    /**
     * Tìm kiếm giao dịch theo từ khóa
     */
    public List<Transaction> searchTransactions(String keyword) {
        return transactionService.searchTransactions(keyword);
    }

    public boolean addTransaction(Transaction t) {
        return transactionService.createTransaction(t);
    }

    public boolean updateTransaction(Transaction t) {
        return transactionService.updateTransaction(t);
    }

    public boolean deleteTransaction(int id) {
        return transactionService.deleteTransaction(id);
    }
}
