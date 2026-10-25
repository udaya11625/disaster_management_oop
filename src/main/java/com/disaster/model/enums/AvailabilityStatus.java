package com.disaster.model.enums;

public enum AvailabilityStatus {
    AVAILABLE("Available for Deployment", "bg-success text-white"),
    DEPLOYED("Currently Deployed", "bg-primary text-white"),
    ON_MISSION("On Active Mission", "bg-warning text-dark"),
    OFF_DUTY("Off Duty / Resting", "bg-secondary text-white"),
    UNAVAILABLE("Unavailable", "bg-danger text-white");

    private final String displayName;
    private final String badgeClass;

    AvailabilityStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public static AvailabilityStatus fromString(String status) {
        if (status == null) return AVAILABLE;
        try {
            return AvailabilityStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return AVAILABLE;
        }
    }
}

