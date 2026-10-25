package com.disaster.model.enums;

public enum Severity {
    LOW("Low", 1, "bg-secondary text-white", "Minor incident, minimal risk to life or infrastructure"),
    MEDIUM("Medium", 2, "bg-info text-dark", "Moderate situation requiring monitoring and localized response"),
    HIGH("High", 3, "bg-warning text-dark", "Severe danger to life or property, urgent action required"),
    CRITICAL("Critical", 4, "bg-danger text-white", "Catastrophic, immediate life-threatening emergency");

    private final String displayName;
    private final int level;
    private final String badgeClass;
    private final String description;

    Severity(String displayName, int level, String badgeClass, String description) {
        this.displayName = displayName;
        this.level = level;
        this.badgeClass = badgeClass;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public String getDescription() {
        return description;
    }

    public static Severity fromString(String sev) {
        if (sev == null) return MEDIUM;
        try {
            return Severity.valueOf(sev.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return MEDIUM;
        }
    }
}

