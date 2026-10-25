package com.disaster.model.enums;

public enum RequestStatus {
    PENDING("Pending Dispatch", "bg-warning text-dark"),
    ASSIGNED("Team Assigned", "bg-info text-dark"),
    IN_PROGRESS("In Progress / Rescuing", "bg-primary text-white"),
    RESOLVED("Resolved / Safely Evacuated", "bg-success text-white");

    private final String displayName;
    private final String badgeClass;

    RequestStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public static RequestStatus fromString(String status) {
        if (status == null) return PENDING;
        try {
            return RequestStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }
}

