package utils.enums;

/**
 * Enum cho trạng thái khóa học
 */
public enum CourseStatus {
    DANG_MO("Đang mở"),
    NGUNG_MO("Ngừng mở"),
    HET_CHO("Hết chỗ");

    private final String displayName;

    CourseStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CourseStatus fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return DANG_MO;
        }
        for (CourseStatus status : CourseStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return DANG_MO;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
