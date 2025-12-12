package ua.edu.viti.military.entity;

/**
 * Enum representing driver status.
 */
public enum DriverStatus {
    ACTIVE("Активний"),
    INACTIVE("Неактивний"),
    ON_LEAVE("У відпустці"),
    SUSPENDED("Відсторонений");

    private final String displayName;

    DriverStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
