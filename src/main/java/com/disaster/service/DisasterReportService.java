package com.disaster.service;

import com.disaster.dao.DisasterReportDAO;
import com.disaster.model.DisasterReport;
import com.disaster.model.enums.ReportStatus;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service Layer for Disaster Reporting and Incident Coordination.
 */
public class DisasterReportService {
    private final DisasterReportDAO reportDAO;

    public DisasterReportService() {
        this.reportDAO = new DisasterReportDAO();
    }

    public DisasterReportService(DisasterReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }

    public DisasterReport submitReport(DisasterReport report) {
        if (report.getCode() == null || report.getCode().isEmpty()) {
            report.setCode(generateReportCode());
        }
        if (report.getStatus() == null) {
            report.setStatus(ReportStatus.PENDING);
        }
        return reportDAO.save(report);
    }

    public List<DisasterReport> getAllReports() {
        return reportDAO.findAll();
    }

    public Optional<DisasterReport> getReportById(Long id) {
        return reportDAO.findById(id);
    }

    public Optional<DisasterReport> getReportByCode(String code) {
        return reportDAO.findByCode(code);
    }

    public List<DisasterReport> getReportsByReporter(Long reporterId) {
        return reportDAO.findByReporterId(reporterId);
    }

    public List<DisasterReport> searchReports(String keyword, String type, String severity, String status) {
        return reportDAO.searchReports(keyword, type, severity, status);
    }

    public boolean updateReportStatus(Long reportId, ReportStatus newStatus, Long adminId, String adminNotes) {
        return reportDAO.updateStatus(reportId, newStatus, adminId, adminNotes);
    }

    public boolean updateReport(DisasterReport report) {
        return reportDAO.update(report);
    }

    public boolean deleteReport(Long id) {
        return reportDAO.deleteById(id);
    }

    private String generateReportCode() {
        int year = Year.now().getValue();
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "REP-" + year + "-" + random;
    }
}

