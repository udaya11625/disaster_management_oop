package com.disaster.model.enums;

public enum ShelterStatus {
    ACTIVE("Operational", "bg-success text-white"),
    FULL("At Capacity", "bg-danger text-white"),
    INACTIVE("Closed / Inactive", "bg-secondary text-white");

    private final String displayName;
    private final String badgeClass;

    ShelterStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public static ShelterStatus fromString(String status) {
        if (status == null) return ACTIVE;
        try {
            return ShelterStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ACTIVE;
        }
    }
}

