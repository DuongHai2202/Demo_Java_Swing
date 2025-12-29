package utils.enums;

/**
 * Enum cho loại giao dịch
 */
public enum TransactionType {
    HOC_PHI("Học phí"),
    PHI_GHI_DANH("Phí ghi danh"),
    GIAO_TRINH("Giáo trình"),
    HOAN_TIEN("Hoàn tiền"),
    KHAC("Khác");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Lấy TransactionType từ tên hiển thị (dùng khi đọc từ Database)
     */
    public static TransactionType fromDisplayName(String text) {
        if (text == null || text.trim().isEmpty()) {
            return HOC_PHI;
        }
        for (TransactionType type : TransactionType.values()) {
            if (type.displayName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return KHAC;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
