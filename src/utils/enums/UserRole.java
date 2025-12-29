package utils.enums;

/**
 * Enum cho vai trò người dùng
 */
public enum UserRole {
    QUAN_TRI_VIEN("Quản trị viên"),
    NHAN_VIEN("Nhân viên"),
    GIANG_VIEN("Giảng viên"),
    HOC_VIEN("Học viên");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Lấy UserRole từ tên hiển thị (dùng khi đọc từ Database)
     */
    public static UserRole fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return HOC_VIEN;
        }
        for (UserRole role : UserRole.values()) {
            if (role.displayName.equalsIgnoreCase(text)) {
                return role;
            }
        }
        return HOC_VIEN;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
