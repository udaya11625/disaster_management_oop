package com.disaster.model;

import com.disaster.model.enums.DisasterType;

import java.sql.Timestamp;

/**
 * DisasterUpdate domain model.
 * Represents official broadcast bulletins, alerts, and public safety announcements.
 */
public class DisasterUpdate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String title;
    private String content;
    private DisasterType disasterType;
    private String severity; // INFORMATION, WARNING, CRITICAL
    private String affectedAreas;
    private Long issuedBy;
    private String issuerName;
    private Timestamp publishedAt;
    private boolean active = true;

    public DisasterUpdate() {
        super();
        this.publishedAt = new Timestamp(System.currentTimeMillis());
    }

    public DisasterUpdate(Long id, String title, String content, DisasterType disasterType, 
                          String severity, String affectedAreas, Long issuedBy) {
        super(id);
        this.title = title;
        this.content = content;
        this.disasterType = disasterType;
        this.severity = severity;
        this.affectedAreas = affectedAreas;
        this.issuedBy = issuedBy;
        this.publishedAt = new Timestamp(System.currentTimeMillis());
        this.active = true;
    }

    public String getSeverityBadge() {
        if ("CRITICAL".equalsIgnoreCase(severity)) {
            return "<span class=\"badge bg-danger animate-pulse\"><i class=\"bi bi-exclamation-octagon-fill me-1\"></i>CRITICAL WARNING</span>";
        } else if ("WARNING".equalsIgnoreCase(severity)) {
            return "<span class=\"badge bg-warning text-dark\"><i class=\"bi bi-exclamation-triangle-fill me-1\"></i>ADVISORY</span>";
        } else {
            return "<span class=\"badge bg-info text-dark\"><i class=\"bi bi-info-circle-fill me-1\"></i>BULLETIN</span>";
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DisasterType getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(DisasterType disasterType) {
        this.disasterType = disasterType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getAffectedAreas() {
        return affectedAreas;
    }

    public void setAffectedAreas(String affectedAreas) {
        this.affectedAreas = affectedAreas;
    }

    public Long getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(Long issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public Timestamp getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Timestamp publishedAt) {
        this.publishedAt = publishedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

