package models;

/**
 * Enum cho trạng thái đăng ký học
 */
public enum EnrollmentStatus {
    DANG_CHO("Đang chờ"),
    DA_XAC_NHAN("Đã xác nhận"),
    DA_HUY("Đã hủy"),
    DA_HOAN_THANH("Đã hoàn thành");

    private final String displayName;

    EnrollmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Lấy EnrollmentStatus từ tên hiển thị (dùng khi đọc từ Database)
     */
    public static EnrollmentStatus fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return DANG_CHO; // Default
        }
        for (EnrollmentStatus status : EnrollmentStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return DANG_CHO; // Default if not found
    }

    @Override
    public String toString() {
        return displayName;
    }
}
