package utils.enums;

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

    public static SupportStatus fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return MOI;
        }
        for (SupportStatus status : SupportStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return MOI;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
