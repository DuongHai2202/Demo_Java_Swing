package utils.enums;

/**
 * Enum cho trạng thái người dùng
 */
public enum UserStatus {
    DANG_HOAT_DONG("Đang hoạt động"),
    NGUNG_HOAT_DONG("Ngừng hoạt động"),
    TAM_KHOA("Tạm khóa");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Lấy UserStatus từ tên hiển thị (dùng khi đọc từ Database)
     */
    public static UserStatus fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return DANG_HOAT_DONG;
        }
        for (UserStatus status : UserStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return DANG_HOAT_DONG;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
