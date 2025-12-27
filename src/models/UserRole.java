package models;

import java.sql.Timestamp;

/**
 * Enum cho vai trò người dùng (Bản Tiếng Việt)
 */
public enum UserRole {
    QUAN_TRI_VIEN("Quản trị viên"),
    NHAN_VIEN("Nhân viên"),
    GIANG_VIEN("Giảng viên"),
    HOC_VIEN("Học viên"),
    KHACH("Khách");

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
        for (UserRole role : UserRole.values()) {
            if (role.displayName.equalsIgnoreCase(text)) {
                return role;
            }
        }
        return KHACH; // Default to Guest if not found
    }
}
