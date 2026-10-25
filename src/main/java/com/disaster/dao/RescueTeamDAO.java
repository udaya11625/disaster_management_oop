package com.disaster.dao;

import com.disaster.model.RescueTeam;
import com.disaster.model.enums.AvailabilityStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Rescue Teams.
 */
public class RescueTeamDAO extends AbstractDAO<RescueTeam> implements GenericDAO<RescueTeam, Long> {

    @Override
    protected RescueTeam mapResultSetToEntity(ResultSet rs) throws SQLException {
        RescueTeam team = new RescueTeam();
        team.setId(rs.getLong("id"));
        team.setTeamName(rs.getString("team_name"));
        team.setTeamLeadId(rs.getObject("team_lead_id") != null ? rs.getLong("team_lead_id") : null);
        team.setContactNumber(rs.getString("contact_number"));
        team.setSpecialization(rs.getString("specialization"));
        team.setMemberCount(rs.getInt("member_count"));
        team.setOperationalArea(rs.getString("operational_area"));
        team.setAvailability(AvailabilityStatus.fromString(rs.getString("availability")));
        team.setCreatedAt(rs.getTimestamp("created_at"));

        try {
            team.setTeamLeadName(rs.getString("team_lead_name"));
        } catch (SQLException ignored) {}

        return team;
    }

    @Override
    public Optional<RescueTeam> findById(Long id) {
        String sql = "SELECT rt.*, u.full_name as team_lead_name " +
                     "FROM rescue_teams rt " +
                     "LEFT JOIN users u ON rt.team_lead_id = u.id " +
                     "WHERE rt.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding rescue team by id {}: {}", id, e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<RescueTeam> findAll() {
        List<RescueTeam> list = new ArrayList<>();
        String sql = "SELECT rt.*, u.full_name as team_lead_name " +
                     "FROM rescue_teams rt " +
                     "LEFT JOIN users u ON rt.team_lead_id = u.id " +
                     "ORDER BY rt.team_name ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all rescue teams: {}", e.getMessage(), e);
        }
        return list;
    }

    public List<RescueTeam> findAvailableTeams() {
        List<RescueTeam> list = new ArrayList<>();
        String sql = "SELECT rt.*, u.full_name as team_lead_name " +
                     "FROM rescue_teams rt " +
                     "LEFT JOIN users u ON rt.team_lead_id = u.id " +
                     "WHERE rt.availability = 'AVAILABLE' ORDER BY rt.team_name ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding available rescue teams: {}", e.getMessage(), e);
        }
        return list;
    }

    @Override
    public RescueTeam save(RescueTeam team) {
        String sql = "INSERT INTO rescue_teams (team_name, team_lead_id, contact_number, specialization, " +
                     "member_count, operational_area, availability, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, team.getTeamName());
            if (team.getTeamLeadId() != null) ps.setLong(2, team.getTeamLeadId()); else ps.setNull(2, Types.BIGINT);
            ps.setString(3, team.getContactNumber());
            ps.setString(4, team.getSpecialization());
            ps.setInt(5, team.getMemberCount());
            ps.setString(6, team.getOperationalArea());
            ps.setString(7, team.getAvailability() != null ? team.getAvailability().name() : "AVAILABLE");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        team.setId(rs.getLong(1));
                    }
                }
            }
            return team;
        } catch (SQLException e) {
            logger.error("Error saving rescue team: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to persist rescue team: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(RescueTeam team) {
        String sql = "UPDATE rescue_teams SET team_name = ?, team_lead_id = ?, contact_number = ?, " +
                     "specialization = ?, member_count = ?, operational_area = ?, availability = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, team.getTeamName());
            if (team.getTeamLeadId() != null) ps.setLong(2, team.getTeamLeadId()); else ps.setNull(2, Types.BIGINT);
            ps.setString(3, team.getContactNumber());
            ps.setString(4, team.getSpecialization());
            ps.setInt(5, team.getMemberCount());
            ps.setString(6, team.getOperationalArea());
            ps.setString(7, team.getAvailability().name());
            ps.setLong(8, team.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating rescue team {}: {}", team.getId(), e.getMessage(), e);
            return false;
        }
    }

    public boolean updateAvailability(Long teamId, AvailabilityStatus status) {
        String sql = "UPDATE rescue_teams SET availability = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, teamId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating rescue team availability: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM rescue_teams WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting rescue team {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
}

