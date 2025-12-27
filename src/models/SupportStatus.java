package models;

/**
 * Enum cho trạng thái yêu cầu hỗ trợ
 */
public enum SupportStatus {
    MOI("Mới"),
    DANG_XU_LY("Đang xử lý"),
    DA_GIAI_QUYET("Đã giải quyết"),
    DA_DONG("Đã đóng");

    private final String displayName;

    SupportStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Lấy SupportStatus từ tên hiển thị (dùng khi đọc từ Database)
     */
    public static SupportStatus fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return MOI; // Default
        }
        for (SupportStatus status : SupportStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return MOI; // Default if not found
    }

    @Override
    public String toString() {
        return displayName;
    }
}
