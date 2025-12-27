package models;

public enum CourseStatus {
    DANG_MO("Đang mở"),
    DA_DONG("Đã đóng");

    private final String displayName;

    CourseStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CourseStatus fromDisplayName(String text) {
        for (CourseStatus status : CourseStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        // Fallback for legacy data or if not found
        if ("ACTIVE".equalsIgnoreCase(text))
            return DANG_MO;
        if ("INACTIVE".equalsIgnoreCase(text))
            return DA_DONG;

        return DANG_MO; // Default
    }

    @Override
    public String toString() {
        return displayName;
    }
}
