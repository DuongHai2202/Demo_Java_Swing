package models;

/**
 * Enum cho phương thức thanh toán
 */
public enum PaymentMethod {
    TIEN_MAT("Tiền mặt"),
    CHUYEN_KHOAN("Chuyển khoản"),
    THE_TIN_DUNG("Thẻ tín dụng"),
    VI_DIEN_TU("Ví điện tử");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Lấy PaymentMethod từ tên hiển thị (dùng khi đọc từ Database)
     */
    public static PaymentMethod fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return TIEN_MAT; // Default
        }
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.displayName.equalsIgnoreCase(text)) {
                return method;
            }
        }
        return TIEN_MAT; // Default if not found
    }

    @Override
    public String toString() {
        return displayName;
    }
}
