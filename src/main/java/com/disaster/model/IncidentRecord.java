package com.disaster.model;

import com.disaster.model.enums.DisasterType;
import com.disaster.model.enums.Severity;

/**
 * Abstract IncidentRecord demonstrating INHERITANCE and ABSTRACTION.
 * Represents core properties shared between citizen Disaster Reports and Assistance Requests.
 */
public abstract class IncidentRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String code;
    private DisasterType disasterType;
    private String location;
    private String landmark;
    private Severity severity;

    public IncidentRecord() {
        super();
    }

    public IncidentRecord(Long id, String code, DisasterType disasterType, 
                          String location, String landmark, Severity severity) {
        super(id);
        this.code = code;
        this.disasterType = disasterType;
        this.location = location;
        this.landmark = landmark;
        this.severity = severity;
    }

    // Abstract methods demonstrating POLYMORPHISM
    public abstract String getReferenceCode();
    public abstract int calculatePriorityScore();
    public abstract boolean isResolved();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DisasterType getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(DisasterType disasterType) {
        this.disasterType = disasterType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public boolean isCritical() {
        return this.severity == Severity.CRITICAL;
    }
}

