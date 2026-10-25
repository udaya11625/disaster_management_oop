package com.disaster.model.enums;

public enum Role {
    CITIZEN("Citizen", "Community member reporting incidents or seeking help"),
    VOLUNTEER("Volunteer", "Community volunteer assisting in relief operations"),
    RESCUE_TEAM("Rescue Team Member", "Specialized rapid response emergency personnel"),
    ADMIN("Administrator", "Disaster management authority and system coordinator");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static Role fromString(String roleStr) {
        if (roleStr == null) return CITIZEN;
        try {
            return Role.valueOf(roleStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return CITIZEN;
        }
    }
}

