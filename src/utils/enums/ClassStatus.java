package utils.enums;

/**
 * Enum cho trạng thái lớp học
 */
public enum ClassStatus {
    CHUA_BAT_DAU("Chưa bắt đầu"),
    DANG_DIEN_RA("Đang diễn ra"),
    DA_KET_THUC("Đã kết thúc"),
    BI_HUY("Bị hủy");

    private final String displayName;

    ClassStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ClassStatus fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return CHUA_BAT_DAU;
        }
        for (ClassStatus status : ClassStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return CHUA_BAT_DAU;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
