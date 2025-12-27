package models;

/**
 * Enum cho trạng thái thanh toán
 */
public enum PaymentStatus {
    CHUA_THANH_TOAN("Chưa thanh toán"),
    THANH_TOAN_MOT_PHAN("Thanh toán một phần"),
    DA_THANH_TOAN("Đã thanh toán");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Lấy PaymentStatus từ tên hiển thị (dùng khi đọc từ Database)
     */
    public static PaymentStatus fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return CHUA_THANH_TOAN; // Default
        }
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return CHUA_THANH_TOAN; // Default if not found
    }

    @Override
    public String toString() {
        return displayName;
    }
}
