package com.disaster.model.enums;

public enum ResourceCategory {
    FOOD_WATER("Food & Clean Water", "bi-basket", "bg-primary text-white"),
    MEDICAL("Medical & First Aid", "bi-heart-pulse", "bg-danger text-white"),
    CLOTHING_BEDDING("Clothing & Bedding", "bi-layers", "bg-info text-dark"),
    RESCUE_EQUIPMENT("Rescue Tools & Gear", "bi-tools", "bg-warning text-dark"),
    SANITATION("Sanitation & Hygiene", "bi-shield-check", "bg-success text-white"),
    OTHER("General Supplies", "bi-box-seam", "bg-secondary text-white");

    private final String displayName;
    private final String iconClass;
    private final String badgeClass;

    ResourceCategory(String displayName, String iconClass, String badgeClass) {
        this.displayName = displayName;
        this.iconClass = iconClass;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIconClass() {
        return iconClass;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public static ResourceCategory fromString(String category) {
        if (category == null) return OTHER;
        try {
            return ResourceCategory.valueOf(category.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}

