package com.disaster.model.enums;

public enum ReportStatus {
    PENDING("Pending Verification", "bg-warning text-dark"),
    VERIFIED("Verified", "bg-info text-dark"),
    RESPONDING("Units Responding", "bg-primary text-white"),
    RESOLVED("Resolved", "bg-success text-white"),
    DISMISSED("Dismissed", "bg-secondary text-white");

    private final String displayName;
    private final String badgeClass;

    ReportStatus(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public static ReportStatus fromString(String status) {
        if (status == null) return PENDING;
        try {
            return ReportStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }
}

