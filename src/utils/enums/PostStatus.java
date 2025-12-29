package utils.enums;

/**
 * Enum cho trạng thái bài viết
 */
public enum PostStatus {
    BAN_NHAP("Bản nháp"),
    DA_DANG("Đã đăng"),
    LUU_TRU("Lưu trữ");

    private final String displayName;

    PostStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PostStatus fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return BAN_NHAP;
        }
        for (PostStatus status : PostStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return BAN_NHAP;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
