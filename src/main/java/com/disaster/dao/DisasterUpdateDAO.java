package com.disaster.dao;

import com.disaster.model.DisasterUpdate;
import com.disaster.model.enums.DisasterType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Official Advisories and Disaster Updates.
 */
public class DisasterUpdateDAO extends AbstractDAO<DisasterUpdate> implements GenericDAO<DisasterUpdate, Long> {

    @Override
    protected DisasterUpdate mapResultSetToEntity(ResultSet rs) throws SQLException {
        DisasterUpdate upd = new DisasterUpdate();
        upd.setId(rs.getLong("id"));
        upd.setTitle(rs.getString("title"));
        upd.setContent(rs.getString("content"));
        upd.setDisasterType(DisasterType.fromString(rs.getString("disaster_type")));
        upd.setSeverity(rs.getString("severity"));
        upd.setAffectedAreas(rs.getString("affected_areas"));
        upd.setIssuedBy(rs.getObject("issued_by") != null ? rs.getLong("issued_by") : null);
        upd.setPublishedAt(rs.getTimestamp("published_at"));
        upd.setActive(rs.getBoolean("is_active"));

        try {
            upd.setIssuerName(rs.getString("issuer_name"));
        } catch (SQLException ignored) {}

        return upd;
    }

    @Override
    public Optional<DisasterUpdate> findById(Long id) {
        String sql = "SELECT du.*, u.full_name as issuer_name FROM disaster_updates du " +
                     "LEFT JOIN users u ON du.issued_by = u.id WHERE du.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding update by id {}: {}", id, e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<DisasterUpdate> findAll() {
        List<DisasterUpdate> list = new ArrayList<>();
        String sql = "SELECT du.*, u.full_name as issuer_name FROM disaster_updates du " +
                     "LEFT JOIN users u ON du.issued_by = u.id ORDER BY du.published_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all updates: {}", e.getMessage(), e);
        }
        return list;
    }

    public List<DisasterUpdate> findActiveUpdates() {
        List<DisasterUpdate> list = new ArrayList<>();
        String sql = "SELECT du.*, u.full_name as issuer_name FROM disaster_updates du " +
                     "LEFT JOIN users u ON du.issued_by = u.id WHERE du.is_active = true ORDER BY du.published_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding active updates: {}", e.getMessage(), e);
        }
        return list;
    }

    @Override
    public DisasterUpdate save(DisasterUpdate upd) {
        String sql = "INSERT INTO disaster_updates (title, content, disaster_type, severity, " +
                     "affected_areas, issued_by, published_at, is_active) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, upd.getTitle());
            ps.setString(2, upd.getContent());
            ps.setString(3, upd.getDisasterType().name());
            ps.setString(4, upd.getSeverity());
            ps.setString(5, upd.getAffectedAreas());
            if (upd.getIssuedBy() != null) ps.setLong(6, upd.getIssuedBy()); else ps.setNull(6, Types.BIGINT);
            ps.setBoolean(7, upd.isActive());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        upd.setId(rs.getLong(1));
                    }
                }
            }
            return upd;
        } catch (SQLException e) {
            logger.error("Error saving disaster update: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to persist update: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(DisasterUpdate upd) {
        String sql = "UPDATE disaster_updates SET title = ?, content = ?, disaster_type = ?, " +
                     "severity = ?, affected_areas = ?, is_active = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, upd.getTitle());
            ps.setString(2, upd.getContent());
            ps.setString(3, upd.getDisasterType().name());
            ps.setString(4, upd.getSeverity());
            ps.setString(5, upd.getAffectedAreas());
            ps.setBoolean(6, upd.isActive());
            ps.setLong(7, upd.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating update {}: {}", upd.getId(), e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM disaster_updates WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting update {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
}

