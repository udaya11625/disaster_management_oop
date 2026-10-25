package com.disaster.dao;

import com.disaster.model.ReliefResource;
import com.disaster.model.ResourceDistribution;
import com.disaster.model.enums.ResourceCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Relief Resources and Stock Distribution.
 * Employs STRICT TRANSACTIONAL INTEGRITY preventing negative inventory.
 */
public class ReliefResourceDAO extends AbstractDAO<ReliefResource> implements GenericDAO<ReliefResource, Long> {

    @Override
    protected ReliefResource mapResultSetToEntity(ResultSet rs) throws SQLException {
        ReliefResource res = new ReliefResource();
        res.setId(rs.getLong("id"));
        res.setName(rs.getString("name"));
        res.setCategory(ResourceCategory.fromString(rs.getString("category")));
        res.setQuantity(rs.getInt("quantity"));
        res.setUnit(rs.getString("unit"));
        res.setStorageLocation(rs.getString("storage_location"));
        res.setMinStockLevel(rs.getInt("min_stock_level"));
        res.setCreatedAt(rs.getTimestamp("created_at"));
        res.setUpdatedAt(rs.getTimestamp("updated_at"));
        return res;
    }

    @Override
    public Optional<ReliefResource> findById(Long id) {
        String sql = "SELECT * FROM relief_resources WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding relief resource by id {}: {}", id, e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<ReliefResource> findAll() {
        List<ReliefResource> list = new ArrayList<>();
        String sql = "SELECT * FROM relief_resources ORDER BY category ASC, name ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all relief resources: {}", e.getMessage(), e);
        }
        return list;
    }

    public List<ReliefResource> findLowStockResources() {
        List<ReliefResource> list = new ArrayList<>();
        String sql = "SELECT * FROM relief_resources WHERE quantity <= min_stock_level ORDER BY quantity ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding low stock resources: {}", e.getMessage(), e);
        }
        return list;
    }

    public List<ReliefResource> searchResources(String keyword, String category) {
        List<ReliefResource> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM relief_resources WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(name) LIKE ? OR LOWER(storage_location) LIKE ?) ");
            String term = "%" + keyword.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
        }
        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category)) {
            sql.append("AND category = ? ");
            params.add(category.toUpperCase());
        }

        sql.append("ORDER BY category ASC, name ASC");

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
            logger.error("Error searching resources: {}", e.getMessage(), e);
        }
        return list;
    }

    @Override
    public ReliefResource save(ReliefResource resource) {
        String sql = "INSERT INTO relief_resources (name, category, quantity, unit, storage_location, " +
                     "min_stock_level, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, resource.getName());
            ps.setString(2, resource.getCategory().name());
            ps.setInt(3, resource.getQuantity());
            ps.setString(4, resource.getUnit());
            ps.setString(5, resource.getStorageLocation());
            ps.setInt(6, resource.getMinStockLevel());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        resource.setId(rs.getLong(1));
                    }
                }
            }
            return resource;
        } catch (SQLException e) {
            logger.error("Error saving relief resource: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to persist resource: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(ReliefResource resource) {
        String sql = "UPDATE relief_resources SET name = ?, category = ?, quantity = ?, unit = ?, " +
                     "storage_location = ?, min_stock_level = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resource.getName());
            ps.setString(2, resource.getCategory().name());
            ps.setInt(3, resource.getQuantity());
            ps.setString(4, resource.getUnit());
            ps.setString(5, resource.getStorageLocation());
            ps.setInt(6, resource.getMinStockLevel());
            ps.setLong(7, resource.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating resource {}: {}", resource.getId(), e.getMessage(), e);
            return false;
        }
    }

    public boolean replenishStock(Long resourceId, int addAmount) {
        if (addAmount <= 0) return false;
        String sql = "UPDATE relief_resources SET quantity = quantity + ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, addAmount);
            ps.setLong(2, resourceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error replenishing stock for resource {}: {}", resourceId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Atomically distributes stock, preventing negative inventory.
     */
    public boolean recordDistribution(Long resourceId, int quantity, String destType, String destName,
                                      Long shelterId, Long requestId, Long distributedBy, String recipientContact, String remarks) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // 1. Check current inventory under lock
            String checkSql = "SELECT quantity FROM relief_resources WHERE id = ? FOR UPDATE";
            int currentStock = 0;
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setLong(1, resourceId);
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        currentStock = rs.getInt("quantity");
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            if (currentStock < quantity || quantity <= 0) {
                conn.rollback();
                return false; // Prevent negative stock
            }

            // 2. Deduct inventory
            String deductSql = "UPDATE relief_resources SET quantity = quantity - ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            try (PreparedStatement deductPs = conn.prepareStatement(deductSql)) {
                deductPs.setInt(1, quantity);
                deductPs.setLong(2, resourceId);
                deductPs.executeUpdate();
            }

            // 3. Log distribution transaction
            String logSql = "INSERT INTO resource_distributions (resource_id, quantity, destination_type, " +
                            "destination_name, shelter_id, request_id, distributed_by, recipient_contact, remarks, distribution_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
            try (PreparedStatement logPs = conn.prepareStatement(logSql)) {
                logPs.setLong(1, resourceId);
                logPs.setInt(2, quantity);
                logPs.setString(3, destType);
                logPs.setString(4, destName);
                if (shelterId != null) logPs.setLong(5, shelterId); else logPs.setNull(5, Types.BIGINT);
                if (requestId != null) logPs.setLong(6, requestId); else logPs.setNull(6, Types.BIGINT);
                if (distributedBy != null) logPs.setLong(7, distributedBy); else logPs.setNull(7, Types.BIGINT);
                logPs.setString(8, recipientContact);
                logPs.setString(9, remarks);
                logPs.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            logger.error("Error recording distribution for resource {}: {}", resourceId, e.getMessage(), e);
            rollbackQuietly(conn);
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public List<ResourceDistribution> getAllDistributions() {
        List<ResourceDistribution> list = new ArrayList<>();
        String sql = "SELECT rd.*, r.name AS resource_name, r.unit AS resource_unit, u.full_name AS distributed_by_name " +
                     "FROM resource_distributions rd " +
                     "JOIN relief_resources r ON rd.resource_id = r.id " +
                     "LEFT JOIN users u ON rd.distributed_by = u.id " +
                     "ORDER BY rd.distribution_date DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ResourceDistribution dist = new ResourceDistribution();
                dist.setId(rs.getLong("id"));
                dist.setResourceId(rs.getLong("resource_id"));
                dist.setResourceName(rs.getString("resource_name"));
                dist.setResourceUnit(rs.getString("resource_unit"));
                dist.setQuantity(rs.getInt("quantity"));
                dist.setDestinationType(rs.getString("destination_type"));
                dist.setDestinationName(rs.getString("destination_name"));
                dist.setShelterId(rs.getObject("shelter_id") != null ? rs.getLong("shelter_id") : null);
                dist.setRequestId(rs.getObject("request_id") != null ? rs.getLong("request_id") : null);
                dist.setDistributedBy(rs.getObject("distributed_by") != null ? rs.getLong("distributed_by") : null);
                dist.setDistributedByName(rs.getString("distributed_by_name"));
                dist.setRecipientContact(rs.getString("recipient_contact"));
                dist.setRemarks(rs.getString("remarks"));
                dist.setDistributionDate(rs.getTimestamp("distribution_date"));
                list.add(dist);
            }
        } catch (SQLException e) {
            logger.error("Error finding all distributions: {}", e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM relief_resources WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting resource {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
}

