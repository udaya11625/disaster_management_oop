package com.disaster.model;

import com.disaster.model.enums.DisasterType;
import com.disaster.model.enums.ReportStatus;
import com.disaster.model.enums.Severity;

import java.sql.Timestamp;

/**
 * DisasterReport domain model extending IncidentRecord.
 * Encapsulates public incident reports submitted by citizens.
 */
public class DisasterReport extends IncidentRecord {
    private static final long serialVersionUID = 1L;

    private Long reporterId;
    private String reporterName;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private String imageUrl;
    private ReportStatus status = ReportStatus.PENDING;
    private Long verifiedBy;
    private String verifiedByName;
    private String adminNotes;
    private Timestamp incidentDate;

    public DisasterReport() {
        super();
        this.incidentDate = new Timestamp(System.currentTimeMillis());
    }

    public DisasterReport(Long id, String reportCode, DisasterType disasterType,
                          String title, String description, String location, String landmark,
                          Severity severity, Long reporterId) {
        super(id, reportCode, disasterType, location, landmark, severity);
        this.title = title;
        this.description = description;
        this.reporterId = reporterId;
        this.status = ReportStatus.PENDING;
        this.incidentDate = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String getReferenceCode() {
        return getCode();
    }

    @Override
    public int calculatePriorityScore() {
        int base = getSeverity() != null ? getSeverity().getLevel() * 25 : 50;
        if (status == ReportStatus.PENDING) base += 10;
        return base;
    }

    @Override
    public boolean isResolved() {
        return status == ReportStatus.RESOLVED || status == ReportStatus.DISMISSED;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public Long getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Long verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public String getVerifiedByName() {
        return verifiedByName;
    }

    public void setVerifiedByName(String verifiedByName) {
        this.verifiedByName = verifiedByName;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public Timestamp getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Timestamp incidentDate) {
        this.incidentDate = incidentDate;
    }
}

