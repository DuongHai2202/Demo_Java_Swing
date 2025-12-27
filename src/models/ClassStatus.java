package models;

/**
 * Enum cho trạng thái lớp học
 */
public enum ClassStatus {
    SAP_MO("Sắp mở"),
    DANG_MO_LOP("Đang mở lớp"),
    DA_KET_THUC("Đã kết thúc"),
    DA_HUY("Đã hủy");

    private final String displayName;

    ClassStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Lấy ClassStatus từ tên hiển thị (dùng khi đọc từ Database)
     */
    public static ClassStatus fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return SAP_MO; // Default
        }
        for (ClassStatus status : ClassStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return SAP_MO; // Default if not found
    }

    @Override
    public String toString() {
        return displayName;
    }
}
