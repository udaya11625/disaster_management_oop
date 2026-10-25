package com.disaster.dao;

import com.disaster.model.AssistanceRequest;
import com.disaster.model.enums.DisasterType;
import com.disaster.model.enums.RequestStatus;
import com.disaster.model.enums.Severity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Emergency Assistance Requests.
 */
public class AssistanceRequestDAO extends AbstractDAO<AssistanceRequest> implements GenericDAO<AssistanceRequest, Long> {

    @Override
    protected AssistanceRequest mapResultSetToEntity(ResultSet rs) throws SQLException {
        AssistanceRequest req = new AssistanceRequest();
        req.setId(rs.getLong("id"));
        req.setCode(rs.getString("request_code"));
        req.setCitizenId(rs.getObject("citizen_id") != null ? rs.getLong("citizen_id") : null);
        req.setContactName(rs.getString("contact_name"));
        req.setContactPhone(rs.getString("contact_phone"));
        req.setContactEmail(rs.getString("contact_email"));
        req.setDisasterType(DisasterType.fromString(rs.getString("disaster_type")));
        req.setLocation(rs.getString("location"));
        req.setLandmark(rs.getString("landmark"));
        req.setPeopleAffected(rs.getInt("people_affected"));
        req.setAssistanceType(rs.getString("assistance_type"));
        req.setSeverity(Severity.fromString(rs.getString("priority")));
        req.setSpecialNeeds(rs.getString("special_needs"));
        req.setAssignedTeamId(rs.getObject("assigned_team_id") != null ? rs.getLong("assigned_team_id") : null);
        req.setStatus(RequestStatus.fromString(rs.getString("status")));
        req.setResolutionNotes(rs.getString("resolution_notes"));
        req.setCreatedAt(rs.getTimestamp("created_at"));
        req.setUpdatedAt(rs.getTimestamp("updated_at"));

        try {
            req.setAssignedTeamName(rs.getString("team_name"));
        } catch (SQLException ignored) {}

        return req;
    }

    @Override
    public Optional<AssistanceRequest> findById(Long id) {
        String sql = "SELECT ar.*, rt.team_name " +
                     "FROM assistance_requests ar " +
                     "LEFT JOIN rescue_teams rt ON ar.assigned_team_id = rt.id " +
                     "WHERE ar.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding request by id {}: {}", id, e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<AssistanceRequest> findByCode(String code) {
        String sql = "SELECT ar.*, rt.team_name " +
                     "FROM assistance_requests ar " +
                     "LEFT JOIN rescue_teams rt ON ar.assigned_team_id = rt.id " +
                     "WHERE ar.request_code = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding request by code {}: {}", code, e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<AssistanceRequest> findAll() {
        List<AssistanceRequest> list = new ArrayList<>();
        String sql = "SELECT ar.*, rt.team_name " +
                     "FROM assistance_requests ar " +
                     "LEFT JOIN rescue_teams rt ON ar.assigned_team_id = rt.id " +
                     "ORDER BY ar.created_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all assistance requests: {}", e.getMessage(), e);
        }
        return list;
    }

    public List<AssistanceRequest> findByCitizenId(Long citizenId) {
        List<AssistanceRequest> list = new ArrayList<>();
        String sql = "SELECT ar.*, rt.team_name " +
                     "FROM assistance_requests ar " +
                     "LEFT JOIN rescue_teams rt ON ar.assigned_team_id = rt.id " +
                     "WHERE ar.citizen_id = ? ORDER BY ar.created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, citizenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding requests for citizen {}: {}", citizenId, e.getMessage(), e);
        }
        return list;
    }

    public List<AssistanceRequest> findByAssignedTeamId(Long teamId) {
        List<AssistanceRequest> list = new ArrayList<>();
        String sql = "SELECT ar.*, rt.team_name " +
                     "FROM assistance_requests ar " +
                     "LEFT JOIN rescue_teams rt ON ar.assigned_team_id = rt.id " +
                     "WHERE ar.assigned_team_id = ? ORDER BY ar.created_at DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, teamId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding requests for rescue team {}: {}", teamId, e.getMessage(), e);
        }
        return list;
    }

    public List<AssistanceRequest> searchRequests(String keyword, String type, String priority, String status) {
        List<AssistanceRequest> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT ar.*, rt.team_name " +
            "FROM assistance_requests ar " +
            "LEFT JOIN rescue_teams rt ON ar.assigned_team_id = rt.id " +
            "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(ar.request_code) LIKE ? OR LOWER(ar.contact_name) LIKE ? OR LOWER(ar.location) LIKE ? OR LOWER(ar.special_needs) LIKE ?) ");
            String term = "%" + keyword.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
            params.add(term);
        }
        if (type != null && !type.trim().isEmpty() && !"ALL".equalsIgnoreCase(type)) {
            sql.append("AND ar.disaster_type = ? ");
            params.add(type.toUpperCase());
        }
        if (priority != null && !priority.trim().isEmpty() && !"ALL".equalsIgnoreCase(priority)) {
            sql.append("AND ar.priority = ? ");
            params.add(priority.toUpperCase());
        }
        if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            sql.append("AND ar.status = ? ");
            params.add(status.toUpperCase());
        }

        sql.append("ORDER BY CASE WHEN ar.priority = 'CRITICAL' THEN 1 WHEN ar.priority = 'HIGH' THEN 2 ELSE 3 END, ar.created_at DESC");

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
            logger.error("Error searching assistance requests: {}", e.getMessage(), e);
        }
        return list;
    }

    @Override
    public AssistanceRequest save(AssistanceRequest req) {
        String sql = "INSERT INTO assistance_requests (request_code, citizen_id, contact_name, contact_phone, " +
                     "contact_email, disaster_type, location, landmark, people_affected, assistance_type, priority, " +
                     "special_needs, assigned_team_id, status, resolution_notes, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, req.getCode());
            if (req.getCitizenId() != null) ps.setLong(2, req.getCitizenId()); else ps.setNull(2, Types.BIGINT);
            ps.setString(3, req.getContactName());
            ps.setString(4, req.getContactPhone());
            ps.setString(5, req.getContactEmail());
            ps.setString(6, req.getDisasterType().name());
            ps.setString(7, req.getLocation());
            ps.setString(8, req.getLandmark());
            ps.setInt(9, req.getPeopleAffected());
            ps.setString(10, req.getAssistanceType());
            ps.setString(11, req.getSeverity().name());
            ps.setString(12, req.getSpecialNeeds());
            if (req.getAssignedTeamId() != null) ps.setLong(13, req.getAssignedTeamId()); else ps.setNull(13, Types.BIGINT);
            ps.setString(14, req.getStatus() != null ? req.getStatus().name() : "PENDING");
            ps.setString(15, req.getResolutionNotes());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        req.setId(rs.getLong(1));
                    }
                }
            }
            return req;
        } catch (SQLException e) {
            logger.error("Error saving assistance request: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to persist request: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(AssistanceRequest req) {
        String sql = "UPDATE assistance_requests SET contact_name = ?, contact_phone = ?, contact_email = ?, " +
                     "location = ?, landmark = ?, people_affected = ?, assistance_type = ?, priority = ?, " +
                     "special_needs = ?, assigned_team_id = ?, status = ?, resolution_notes = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, req.getContactName());
            ps.setString(2, req.getContactPhone());
            ps.setString(3, req.getContactEmail());
            ps.setString(4, req.getLocation());
            ps.setString(5, req.getLandmark());
            ps.setInt(6, req.getPeopleAffected());
            ps.setString(7, req.getAssistanceType());
            ps.setString(8, req.getSeverity().name());
            ps.setString(9, req.getSpecialNeeds());
            if (req.getAssignedTeamId() != null) ps.setLong(10, req.getAssignedTeamId()); else ps.setNull(10, Types.BIGINT);
            ps.setString(11, req.getStatus().name());
            ps.setString(12, req.getResolutionNotes());
            ps.setLong(13, req.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating assistance request {}: {}", req.getId(), e.getMessage(), e);
            return false;
        }
    }

    public boolean assignTeam(Long requestId, Long teamId) {
        String sql = "UPDATE assistance_requests SET assigned_team_id = ?, status = 'ASSIGNED', updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, teamId);
            ps.setLong(2, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error assigning team {} to request {}: {}", teamId, requestId, e.getMessage(), e);
            return false;
        }
    }

    public boolean updateStatus(Long requestId, RequestStatus status, String resolutionNotes) {
        String sql = "UPDATE assistance_requests SET status = ?, resolution_notes = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setString(2, resolutionNotes);
            ps.setLong(3, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating assistance request status: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM assistance_requests WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting assistance request {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
}

