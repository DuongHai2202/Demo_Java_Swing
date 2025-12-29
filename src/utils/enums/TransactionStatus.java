package utils.enums;

/**
 * Enum cho trạng thái giao dịch
 */
public enum TransactionStatus {
    DANG_CHO("Đang chờ"),
    THANH_CONG("Thành công"),
    THAT_BAI("Thất bại"),
    DA_HOAN_TIEN("Đã hoàn tiền");

    private final String displayName;

    TransactionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Lấy TransactionStatus từ tên hiển thị (dùng khi đọc từ Database)
     */
    public static TransactionStatus fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return DANG_CHO;
        }
        for (TransactionStatus status : TransactionStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return DANG_CHO;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
