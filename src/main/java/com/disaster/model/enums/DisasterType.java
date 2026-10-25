package com.disaster.model.enums;

public enum DisasterType {
    FLOOD("Flood", "Inundation of land normally dry by water overflow", "bi-water", "badge-primary"),
    EARTHQUAKE("Earthquake", "Sudden violent shaking of the ground", "bi-activity", "badge-danger"),
    LANDSLIDE("Landslide", "Collapse of a mass of earth or rock from a mountain", "bi-triangle-half", "badge-warning"),
    CYCLONE("Cyclone / Hurricane", "Violent rotating storm system with destructive winds", "bi-tornado", "badge-info"),
    FIRE("Wildfire / Structural Fire", "Uncontrolled large destructive fire", "bi-fire", "badge-danger"),
    INDUSTRIAL("Industrial / Hazardous Hazard", "Chemical, nuclear, or hazardous material leak", "bi-radioactive", "badge-dark"),
    OTHER("Other Emergency", "Miscellaneous emergency or disaster situation", "bi-exclamation-octagon", "badge-secondary");

    private final String displayName;
    private final String description;
    private final String iconClass;
    private final String badgeColor;

    DisasterType(String displayName, String description, String iconClass, String badgeColor) {
        this.displayName = displayName;
        this.description = description;
        this.iconClass = iconClass;
        this.badgeColor = badgeColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getIconClass() {
        return iconClass;
    }

    public String getBadgeColor() {
        return badgeColor;
    }

    public static DisasterType fromString(String type) {
        if (type == null) return OTHER;
        try {
            return DisasterType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}

