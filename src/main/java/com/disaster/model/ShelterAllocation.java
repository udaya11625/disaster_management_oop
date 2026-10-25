package com.disaster.model;

import java.sql.Timestamp;

/**
 * ShelterAllocation domain model.
 * Records individual families or evacuee batches checked into a shelter.
 */
public class ShelterAllocation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long shelterId;
    private String shelterName;
    private String representativeName;
    private String contactPhone;
    private int peopleCount;
    private Timestamp checkInDate;
    private Timestamp checkOutDate;
    private String status = "CHECKED_IN"; // CHECKED_IN, CHECKED_OUT
    private String notes;

    public ShelterAllocation() {
        super();
        this.checkInDate = new Timestamp(System.currentTimeMillis());
    }

    public ShelterAllocation(Long id, Long shelterId, String representativeName, 
                             String contactPhone, int peopleCount) {
        super(id);
        this.shelterId = shelterId;
        this.representativeName = representativeName;
        this.contactPhone = contactPhone;
        this.peopleCount = peopleCount;
        this.checkInDate = new Timestamp(System.currentTimeMillis());
        this.status = "CHECKED_IN";
    }

    public Long getShelterId() {
        return shelterId;
    }

    public void setShelterId(Long shelterId) {
        this.shelterId = shelterId;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public Timestamp getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Timestamp checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Timestamp getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Timestamp checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isCheckedIn() {
        return "CHECKED_IN".equalsIgnoreCase(this.status);
    }
}

