package com.disaster.model;

import com.disaster.model.enums.AvailabilityStatus;

/**
 * RescueTeam domain model.
 * Represents operational rescue task forces.
 */
public class RescueTeam extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String teamName;
    private Long teamLeadId;
    private String teamLeadName;
    private String contactNumber;
    private String specialization;
    private int memberCount = 5;
    private String operationalArea;
    private AvailabilityStatus availability = AvailabilityStatus.AVAILABLE;

    public RescueTeam() {
        super();
    }

    public RescueTeam(Long id, String teamName, Long teamLeadId, String contactNumber, 
                      String specialization, int memberCount, String operationalArea) {
        super(id);
        this.teamName = teamName;
        this.teamLeadId = teamLeadId;
        this.contactNumber = contactNumber;
        this.specialization = specialization;
        this.memberCount = memberCount;
        this.operationalArea = operationalArea;
        this.availability = AvailabilityStatus.AVAILABLE;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getTeamLeadId() {
        return teamLeadId;
    }

    public void setTeamLeadId(Long teamLeadId) {
        this.teamLeadId = teamLeadId;
    }

    public String getTeamLeadName() {
        return teamLeadName;
    }

    public void setTeamLeadName(String teamLeadName) {
        this.teamLeadName = teamLeadName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getOperationalArea() {
        return operationalArea;
    }

    public void setOperationalArea(String operationalArea) {
        this.operationalArea = operationalArea;
    }

    public AvailabilityStatus getAvailability() {
        return availability;
    }

    public void setAvailability(AvailabilityStatus availability) {
        this.availability = availability;
    }

    public boolean isAvailable() {
        return availability == AvailabilityStatus.AVAILABLE;
    }
}

