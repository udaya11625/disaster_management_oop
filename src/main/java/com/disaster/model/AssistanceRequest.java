package com.disaster.model;

import com.disaster.model.enums.DisasterType;
import com.disaster.model.enums.RequestStatus;
import com.disaster.model.enums.Severity;

/**
 * AssistanceRequest domain model extending IncidentRecord.
 * Encapsulates urgent SOS and rescue/medical/food assistance requests from citizens.
 */
public class AssistanceRequest extends IncidentRecord {
    private static final long serialVersionUID = 1L;

    private Long citizenId;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private int peopleAffected = 1;
    private String assistanceType; // EVACUATION, MEDICAL_AID, FOOD_WATER, SEARCH_RESCUE, TEMPORARY_SHELTER
    private String specialNeeds;
    private Long assignedTeamId;
    private String assignedTeamName;
    private RequestStatus status = RequestStatus.PENDING;
    private String resolutionNotes;

    public AssistanceRequest() {
        super();
    }

    public AssistanceRequest(Long id, String requestCode, DisasterType disasterType,
                             String location, String landmark, Severity priority,
                             String contactName, String contactPhone, int peopleAffected,
                             String assistanceType) {
        super(id, requestCode, disasterType, location, landmark, priority);
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.peopleAffected = peopleAffected;
        this.assistanceType = assistanceType;
        this.status = RequestStatus.PENDING;
    }

    @Override
    public String getReferenceCode() {
        return getCode();
    }

    @Override
    public int calculatePriorityScore() {
        // High people affected + high severity = higher urgency score
        int base = getSeverity() != null ? getSeverity().getLevel() * 20 : 40;
        int peopleFactor = Math.min(peopleAffected * 5, 30);
        int pendingBonus = (status == RequestStatus.PENDING) ? 15 : 0;
        return base + peopleFactor + pendingBonus;
    }

    @Override
    public boolean isResolved() {
        return status == RequestStatus.RESOLVED;
    }

    public Long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Long citizenId) {
        this.citizenId = citizenId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public int getPeopleAffected() {
        return peopleAffected;
    }

    public void setPeopleAffected(int peopleAffected) {
        this.peopleAffected = peopleAffected;
    }

    public String getAssistanceType() {
        return assistanceType;
    }

    public void setAssistanceType(String assistanceType) {
        this.assistanceType = assistanceType;
    }

    public String getSpecialNeeds() {
        return specialNeeds;
    }

    public void setSpecialNeeds(String specialNeeds) {
        this.specialNeeds = specialNeeds;
    }

    public Long getAssignedTeamId() {
        return assignedTeamId;
    }

    public void setAssignedTeamId(Long assignedTeamId) {
        this.assignedTeamId = assignedTeamId;
    }

    public String getAssignedTeamName() {
        return assignedTeamName;
    }

    public void setAssignedTeamName(String assignedTeamName) {
        this.assignedTeamName = assignedTeamName;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    public boolean isAssigned() {
        return this.assignedTeamId != null && this.assignedTeamId > 0;
    }
}

