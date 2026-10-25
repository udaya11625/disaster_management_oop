package com.disaster.dao;

import com.disaster.model.IncidentAssignment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Volunteer Incident Assignments.
 */
public class IncidentAssignmentDAO extends AbstractDAO<IncidentAssignment> implements GenericDAO<IncidentAssignment, Long> {

    @Override
    protected IncidentAssignment mapResultSetToEntity(ResultSet rs) throws SQLException {
        IncidentAssignment assign = new IncidentAssignment();
        assign.setId(rs.getLong("id"));
        assign.setVolunteerId(rs.getLong("volunteer_id"));
        assign.setReportId(rs.getObject("report_id") != null ? rs.getLong("report_id") : null);
        assign.setRequestId(rs.getObject("request_id") != null ? rs.getLong("request_id") : null);
        assign.setTaskTitle(rs.getString("task_title"));
        assign.setTaskDescription(rs.getString("task_description"));
        assign.setAssignedBy(rs.getObject("assigned_by") != null ? rs.getLong("assigned_by") : null);
        assign.setStatus(rs.getString("status"));
        assign.setAssignedAt(rs.getTimestamp("assigned_at"));
        assign.setCompletedAt(rs.getTimestamp("completed_at"));

        try {
            assign.setVolunteerName(rs.getString("volunteer_name"));
            assign.setVolunteerPhone(rs.getString("volunteer_phone"));
        } catch (SQLException ignored) {}
        try {
            assign.setReportTitle(rs.getString("report_title"));
        } catch (SQLException ignored) {}
        try {
            assign.setRequestLocation(rs.getString("request_location"));
        } catch (SQLException ignored) {}

        return assign;
    }

    @Override
    public Optional<IncidentAssignment> findById(Long id) {
        String sql = "SELECT ia.*, u.full_name as volunteer_name, u.phone as volunteer_phone, " +
                     "dr.title as report_title, ar.location as request_location " +
                     "FROM incident_assignments ia " +
                     "JOIN users u ON ia.volunteer_id = u.id " +
                     "LEFT JOIN disaster_reports dr ON ia.report_id = dr.id " +
                     "LEFT JOIN assistance_requests ar ON ia.request_id = ar.id " +
                     "WHERE ia.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding assignment by id {}: {}", id, e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<IncidentAssignment> findAll() {
        List<IncidentAssignment> list = new ArrayList<>();
        String sql = "SELECT ia.*, u.full_name as volunteer_name, u.phone as volunteer_phone, " +
                     "dr.title as report_title, ar.location as request_location " +
                     "FROM incident_assignments ia " +
                     "JOIN users u ON ia.volunteer_id = u.id " +
                     "LEFT JOIN disaster_reports dr ON ia.report_id = dr.id " +
                     "LEFT JOIN assistance_requests ar ON ia.request_id = ar.id " +
                     "ORDER BY ia.assigned_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all assignments: {}", e.getMessage(), e);
        }
        return list;
    }

    public List<IncidentAssignment> findByVolunteerId(Long volunteerId) {
        List<IncidentAssignment> list = new ArrayList<>();
        String sql = "SELECT ia.*, u.full_name as volunteer_name, u.phone as volunteer_phone, " +
                     "dr.title as report_title, ar.location as request_location " +
                     "FROM incident_assignments ia " +
                     "JOIN users u ON ia.volunteer_id = u.id " +
                     "LEFT JOIN disaster_reports dr ON ia.report_id = dr.id " +
                     "LEFT JOIN assistance_requests ar ON ia.request_id = ar.id " +
                     "WHERE ia.volunteer_id = ? ORDER BY ia.assigned_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, volunteerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding assignments for volunteer {}: {}", volunteerId, e.getMessage(), e);
        }
        return list;
    }

    @Override
    public IncidentAssignment save(IncidentAssignment assign) {
        String sql = "INSERT INTO incident_assignments (volunteer_id, report_id, request_id, task_title, " +
                     "task_description, assigned_by, status, assigned_at) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, assign.getVolunteerId());
            if (assign.getReportId() != null) ps.setLong(2, assign.getReportId()); else ps.setNull(2, Types.BIGINT);
            if (assign.getRequestId() != null) ps.setLong(3, assign.getRequestId()); else ps.setNull(3, Types.BIGINT);
            ps.setString(4, assign.getTaskTitle());
            ps.setString(5, assign.getTaskDescription());
            if (assign.getAssignedBy() != null) ps.setLong(6, assign.getAssignedBy()); else ps.setNull(6, Types.BIGINT);
            ps.setString(7, assign.getStatus() != null ? assign.getStatus() : "ASSIGNED");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        assign.setId(rs.getLong(1));
                    }
                }
            }
            return assign;
        } catch (SQLException e) {
            logger.error("Error saving assignment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to persist assignment: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(IncidentAssignment assign) {
        String sql = "UPDATE incident_assignments SET status = ?, completed_at = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assign.getStatus());
            ps.setTimestamp(2, assign.getCompletedAt());
            ps.setLong(3, assign.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating assignment {}: {}", assign.getId(), e.getMessage(), e);
            return false;
        }
    }

    public boolean updateStatus(Long assignmentId, String status) {
        String sql = "UPDATE incident_assignments SET status = ?, " +
                     "completed_at = CASE WHEN ? = 'COMPLETED' THEN CURRENT_TIMESTAMP ELSE NULL END WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, status);
            ps.setLong(3, assignmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating assignment status: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM incident_assignments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting assignment {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
}

