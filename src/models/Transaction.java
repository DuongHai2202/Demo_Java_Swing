package models;

import utils.enums.PaymentMethod;
import utils.enums.TransactionStatus;
import utils.enums.TransactionType;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Transaction - Model giao dịch thanh toán
 */
public class Transaction {
    // ID và thông tin cơ bản
    private int id;
    private int studentId;
    private int enrollmentId;
    private String transactionCode;
    private BigDecimal amount;
    private Timestamp transactionDate;

    // Loại và phương thức
    private TransactionType transactionType;
    private PaymentMethod paymentMethod;
    private TransactionStatus status;

    // Thông tin bổ sung
    private String description;
    private int processedBy;

    // Helper fields từ joins
    private String studentName;
    private String processorName;

    // Constructors
    public Transaction() {
    }

    public Transaction(int id, int studentId, int enrollmentId, String transactionCode,
            BigDecimal amount, Timestamp transactionDate, TransactionType transactionType,
            PaymentMethod paymentMethod, TransactionStatus status, String description, int processedBy) {
        this.id = id;
        this.studentId = studentId;
        this.enrollmentId = enrollmentId;
        this.transactionCode = transactionCode;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.description = description;
        this.processedBy = processedBy;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(int processedBy) {
        this.processedBy = processedBy;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }
}
