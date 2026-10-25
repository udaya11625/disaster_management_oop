package com.disaster.model;

import com.disaster.model.enums.ResourceCategory;

/**
 * ReliefResource domain model demonstrating ENCAPSULATION and INVARIANT VALIDATION.
 * Manages relief stockpile, prevents negative inventory, flags low-stock warnings.
 */
public class ReliefResource extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String name;
    private ResourceCategory category;
    private int quantity;
    private String unit;
    private String storageLocation;
    private int minStockLevel = 50;

    public ReliefResource() {
        super();
    }

    public ReliefResource(Long id, String name, ResourceCategory category, 
                          int quantity, String unit, String storageLocation, int minStockLevel) {
        super(id);
        this.name = name;
        this.category = category;
        this.quantity = Math.max(0, quantity);
        this.unit = unit;
        this.storageLocation = storageLocation;
        this.minStockLevel = minStockLevel;
    }

    // Business Methods (Encapsulated invariants)
    public boolean isLowStock() {
        return this.quantity <= this.minStockLevel;
    }

    public boolean hasSufficientStock(int requested) {
        return requested > 0 && this.quantity >= requested;
    }

    public boolean deductStock(int amount) {
        if (!hasSufficientStock(amount)) return false;
        this.quantity -= amount;
        return true;
    }

    public void replenishStock(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }

    public String getStockBadge() {
        if (this.quantity == 0) {
            return "<span class=\"badge bg-danger\">Out of Stock</span>";
        } else if (isLowStock()) {
            return "<span class=\"badge bg-warning text-dark\"><i class=\"bi bi-exclamation-triangle-fill me-1\"></i>Low Stock</span>";
        } else {
            return "<span class=\"badge bg-success\"><i class=\"bi bi-check-circle me-1\"></i>Optimal</span>";
        }
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceCategory getCategory() {
        return category;
    }

    public void setCategory(ResourceCategory category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity);
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public int getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(int minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
}

