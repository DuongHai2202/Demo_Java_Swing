package utils.enums;

/**
 * Enum cho trạng thái đăng ký khóa học
 */
public enum EnrollmentStatus {
    DANG_HOC("Đang học"),
    DA_HOAN_THANH("Đã hoàn thành"),
    DA_HUY("Đã hủy"),
    TAM_DUNG("Tạm dừng");

    private final String displayName;

    EnrollmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static EnrollmentStatus fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return DANG_HOC;
        }
        for (EnrollmentStatus status : EnrollmentStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return DANG_HOC;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
