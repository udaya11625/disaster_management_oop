package com.disaster.dao;

import com.disaster.model.DisasterReport;
import com.disaster.model.enums.DisasterType;
import com.disaster.model.enums.ReportStatus;
import com.disaster.model.enums.Severity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Disaster Reports.
 */
public class DisasterReportDAO extends AbstractDAO<DisasterReport> implements GenericDAO<DisasterReport, Long> {

    @Override
    protected DisasterReport mapResultSetToEntity(ResultSet rs) throws SQLException {
        DisasterReport report = new DisasterReport();
        report.setId(rs.getLong("id"));
        report.setCode(rs.getString("report_code"));
        report.setReporterId(rs.getObject("reporter_id") != null ? rs.getLong("reporter_id") : null);
        report.setDisasterType(DisasterType.fromString(rs.getString("disaster_type")));
        report.setTitle(rs.getString("title"));
        report.setDescription(rs.getString("description"));
        report.setLocation(rs.getString("location"));
        report.setLandmark(rs.getString("landmark"));
        report.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
        report.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
        report.setSeverity(Severity.fromString(rs.getString("severity")));
        report.setImageUrl(rs.getString("image_url"));
        report.setStatus(ReportStatus.fromString(rs.getString("status")));
        report.setVerifiedBy(rs.getObject("verified_by") != null ? rs.getLong("verified_by") : null);
        report.setAdminNotes(rs.getString("admin_notes"));
        report.setIncidentDate(rs.getTimestamp("incident_date"));
        report.setCreatedAt(rs.getTimestamp("created_at"));
        report.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Join reporter name if available
        try {
            report.setReporterName(rs.getString("reporter_name"));
        } catch (SQLException ignored) {}

        return report;
    }

    @Override
    public Optional<DisasterReport> findById(Long id) {
        String sql = "SELECT dr.*, u.full_name AS reporter_name " +
                     "FROM disaster_reports dr " +
                     "LEFT JOIN users u ON dr.reporter_id = u.id " +
                     "WHERE dr.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding report by id {}: {}", id, e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<DisasterReport> findByCode(String code) {
        String sql = "SELECT dr.*, u.full_name AS reporter_name " +
                     "FROM disaster_reports dr " +
                     "LEFT JOIN users u ON dr.reporter_id = u.id " +
                     "WHERE dr.report_code = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding report by code {}: {}", code, e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<DisasterReport> findAll() {
        List<DisasterReport> list = new ArrayList<>();
        String sql = "SELECT dr.*, u.full_name AS reporter_name " +
                     "FROM disaster_reports dr " +
                     "LEFT JOIN users u ON dr.reporter_id = u.id " +
                     "ORDER BY dr.created_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all reports: {}", e.getMessage(), e);
        }
        return list;
    }

    public List<DisasterReport> findByReporterId(Long reporterId) {
        List<DisasterReport> list = new ArrayList<>();
        String sql = "SELECT dr.*, u.full_name AS reporter_name " +
                     "FROM disaster_reports dr " +
                     "LEFT JOIN users u ON dr.reporter_id = u.id " +
                     "WHERE dr.reporter_id = ? ORDER BY dr.created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, reporterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding reports for reporter {}: {}", reporterId, e.getMessage(), e);
        }
        return list;
    }

    public List<DisasterReport> searchReports(String keyword, String type, String severity, String status) {
        List<DisasterReport> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT dr.*, u.full_name AS reporter_name " +
            "FROM disaster_reports dr " +
            "LEFT JOIN users u ON dr.reporter_id = u.id " +
            "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(dr.title) LIKE ? OR LOWER(dr.description) LIKE ? OR LOWER(dr.location) LIKE ? OR LOWER(dr.report_code) LIKE ?) ");
            String term = "%" + keyword.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
            params.add(term);
        }
        if (type != null && !type.trim().isEmpty() && !"ALL".equalsIgnoreCase(type)) {
            sql.append("AND dr.disaster_type = ? ");
            params.add(type.toUpperCase());
        }
        if (severity != null && !severity.trim().isEmpty() && !"ALL".equalsIgnoreCase(severity)) {
            sql.append("AND dr.severity = ? ");
            params.add(severity.toUpperCase());
        }
        if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            sql.append("AND dr.status = ? ");
            params.add(status.toUpperCase());
        }

        sql.append("ORDER BY dr.created_at DESC");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error searching reports: {}", e.getMessage(), e);
        }
        return list;
    }

    @Override
    public DisasterReport save(DisasterReport report) {
        String sql = "INSERT INTO disaster_reports (report_code, reporter_id, disaster_type, title, description, " +
                     "location, landmark, latitude, longitude, severity, image_url, status, incident_date, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, report.getCode());
            if (report.getReporterId() != null) ps.setLong(2, report.getReporterId()); else ps.setNull(2, Types.BIGINT);
            ps.setString(3, report.getDisasterType().name());
            ps.setString(4, report.getTitle());
            ps.setString(5, report.getDescription());
            ps.setString(6, report.getLocation());
            ps.setString(7, report.getLandmark());
            if (report.getLatitude() != null) ps.setDouble(8, report.getLatitude()); else ps.setNull(8, Types.DOUBLE);
            if (report.getLongitude() != null) ps.setDouble(9, report.getLongitude()); else ps.setNull(9, Types.DOUBLE);
            ps.setString(10, report.getSeverity().name());
            ps.setString(11, report.getImageUrl());
            ps.setString(12, report.getStatus() != null ? report.getStatus().name() : "PENDING");
            ps.setTimestamp(13, report.getIncidentDate() != null ? report.getIncidentDate() : new Timestamp(System.currentTimeMillis()));

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        report.setId(rs.getLong(1));
                    }
                }
            }
            return report;
        } catch (SQLException e) {
            logger.error("Error saving disaster report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to persist report: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(DisasterReport report) {
        String sql = "UPDATE disaster_reports SET disaster_type = ?, title = ?, description = ?, location = ?, " +
                     "landmark = ?, severity = ?, image_url = ?, status = ?, admin_notes = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, report.getDisasterType().name());
            ps.setString(2, report.getTitle());
            ps.setString(3, report.getDescription());
            ps.setString(4, report.getLocation());
            ps.setString(5, report.getLandmark());
            ps.setString(6, report.getSeverity().name());
            ps.setString(7, report.getImageUrl());
            ps.setString(8, report.getStatus().name());
            ps.setString(9, report.getAdminNotes());
            ps.setLong(10, report.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating report {}: {}", report.getId(), e.getMessage(), e);
            return false;
        }
    }

    public boolean updateStatus(Long reportId, ReportStatus status, Long adminId, String adminNotes) {
        String sql = "UPDATE disaster_reports SET status = ?, verified_by = ?, admin_notes = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            if (adminId != null) ps.setLong(2, adminId); else ps.setNull(2, Types.BIGINT);
            ps.setString(3, adminNotes);
            ps.setLong(4, reportId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating report status: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM disaster_reports WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting report {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
}

