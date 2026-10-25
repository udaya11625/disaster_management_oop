package com.disaster.model;

import com.disaster.model.enums.ShelterStatus;

/**
 * Shelter domain model demonstrating strong ENCAPSULATION and INVARIANTS.
 * Prevents over-allocation and encapsulates facility management.
 */
public class Shelter extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String name;
    private String address;
    private String city;
    private String contactPerson;
    private String contactPhone;
    private int totalCapacity;
    private int currentOccupancy;
    private boolean hasFood = true;
    private boolean hasWater = true;
    private boolean hasSanitation = true;
    private boolean hasMedical = false;
    private boolean hasElectricity = true;
    private boolean hasBedding = true;
    private ShelterStatus status = ShelterStatus.ACTIVE;

    public Shelter() {
        super();
    }

    public Shelter(Long id, String name, String address, String city, 
                   String contactPerson, String contactPhone, int totalCapacity) {
        super(id);
        this.name = name;
        this.address = address;
        this.city = city;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.totalCapacity = totalCapacity;
        this.currentOccupancy = 0;
        this.status = ShelterStatus.ACTIVE;
    }

    // Business Methods (Encapsulation of rules)
    public int getAvailableCapacity() {
        return Math.max(0, totalCapacity - currentOccupancy);
    }

    public double getOccupancyRate() {
        if (totalCapacity <= 0) return 0.0;
        return ((double) currentOccupancy / totalCapacity) * 100.0;
    }

    public boolean canAccommodate(int count) {
        if (count <= 0) return false;
        if (status == ShelterStatus.INACTIVE) return false;
        return (currentOccupancy + count) <= totalCapacity;
    }

    public boolean allocate(int count) {
        if (!canAccommodate(count)) return false;
        this.currentOccupancy += count;
        if (this.currentOccupancy >= this.totalCapacity) {
            this.status = ShelterStatus.FULL;
        }
        return true;
    }

    public boolean release(int count) {
        if (count <= 0) return false;
        this.currentOccupancy = Math.max(0, this.currentOccupancy - count);
        if (this.status == ShelterStatus.FULL && this.currentOccupancy < this.totalCapacity) {
            this.status = ShelterStatus.ACTIVE;
        }
        return true;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    public void setCurrentOccupancy(int currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    public boolean isHasFood() {
        return hasFood;
    }

    public void setHasFood(boolean hasFood) {
        this.hasFood = hasFood;
    }

    public boolean isHasWater() {
        return hasWater;
    }

    public void setHasWater(boolean hasWater) {
        this.hasWater = hasWater;
    }

    public boolean isHasSanitation() {
        return hasSanitation;
    }

    public void setHasSanitation(boolean hasSanitation) {
        this.hasSanitation = hasSanitation;
    }

    public boolean isHasMedical() {
        return hasMedical;
    }

    public void setHasMedical(boolean hasMedical) {
        this.hasMedical = hasMedical;
    }

    public boolean isHasElectricity() {
        return hasElectricity;
    }

    public void setHasElectricity(boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
    }

    public boolean isHasBedding() {
        return hasBedding;
    }

    public void setHasBedding(boolean hasBedding) {
        this.hasBedding = hasBedding;
    }

    public ShelterStatus getStatus() {
        return status;
    }

    public void setStatus(ShelterStatus status) {
        this.status = status;
    }
}

