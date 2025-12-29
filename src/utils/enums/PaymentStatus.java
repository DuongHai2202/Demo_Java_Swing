package utils.enums;

/**
 * Enum cho trạng thái thanh toán
 */
public enum PaymentStatus {
    CHUA_THANH_TOAN("Chưa thanh toán"),
    DA_THANH_TOAN("Đã thanh toán"),
    THANH_TOAN_MOT_PHAN("Thanh toán một phần"),
    QUA_HAN("Quá hạn");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentStatus fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return CHUA_THANH_TOAN;
        }
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return CHUA_THANH_TOAN;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
