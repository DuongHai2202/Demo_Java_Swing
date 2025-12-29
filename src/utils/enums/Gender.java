package utils.enums;

/**
 * Enum cho giới tính
 */
public enum Gender {
    NAM("Nam"),
    NU("Nữ"),
    KHAC("Khác");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Lấy Gender từ tên hiển thị (dùng khi đọc từ Database)
     */
    public static Gender fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        for (Gender gender : Gender.values()) {
            if (gender.displayName.equalsIgnoreCase(text)) {
                return gender;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
