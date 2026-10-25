package com.disaster.dao;

import com.disaster.model.Shelter;
import com.disaster.model.ShelterAllocation;
import com.disaster.model.enums.ShelterStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Emergency Shelters.
 * Implements TRANSACTIONAL INTEGRITY to prevent over-allocation.
 */
public class ShelterDAO extends AbstractDAO<Shelter> implements GenericDAO<Shelter, Long> {

    @Override
    protected Shelter mapResultSetToEntity(ResultSet rs) throws SQLException {
        Shelter shelter = new Shelter();
        shelter.setId(rs.getLong("id"));
        shelter.setName(rs.getString("name"));
        shelter.setAddress(rs.getString("address"));
        shelter.setCity(rs.getString("city"));
        shelter.setContactPerson(rs.getString("contact_person"));
        shelter.setContactPhone(rs.getString("contact_phone"));
        shelter.setTotalCapacity(rs.getInt("total_capacity"));
        shelter.setCurrentOccupancy(rs.getInt("current_occupancy"));
        shelter.setHasFood(rs.getBoolean("has_food"));
        shelter.setHasWater(rs.getBoolean("has_water"));
        shelter.setHasSanitation(rs.getBoolean("has_sanitation"));
        shelter.setHasMedical(rs.getBoolean("has_medical"));
        shelter.setHasElectricity(rs.getBoolean("has_electricity"));
        shelter.setHasBedding(rs.getBoolean("has_bedding"));
        shelter.setStatus(ShelterStatus.fromString(rs.getString("status")));
        shelter.setCreatedAt(rs.getTimestamp("created_at"));
        shelter.setUpdatedAt(rs.getTimestamp("updated_at"));
        return shelter;
    }

    @Override
    public Optional<Shelter> findById(Long id) {
        String sql = "SELECT * FROM shelters WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding shelter by id {}: {}", id, e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Shelter> findAll() {
        List<Shelter> list = new ArrayList<>();
        String sql = "SELECT * FROM shelters ORDER BY name ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding all shelters: {}", e.getMessage(), e);
        }
        return list;
    }

    public List<Shelter> findActiveShelters() {
        List<Shelter> list = new ArrayList<>();
        String sql = "SELECT * FROM shelters WHERE status != 'INACTIVE' ORDER BY (total_capacity - current_occupancy) DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error finding active shelters: {}", e.getMessage(), e);
        }
        return list;
    }

    public List<Shelter> searchShelters(String keyword, String city, Boolean medicalOnly) {
        List<Shelter> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM shelters WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(name) LIKE ? OR LOWER(address) LIKE ? OR LOWER(city) LIKE ?) ");
            String term = "%" + keyword.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }
        if (city != null && !city.trim().isEmpty() && !"ALL".equalsIgnoreCase(city)) {
            sql.append("AND LOWER(city) = LOWER(?) ");
            params.add(city.trim());
        }
        if (Boolean.TRUE.equals(medicalOnly)) {
            sql.append("AND has_medical = true ");
        }

        sql.append("ORDER BY status ASC, (total_capacity - current_occupancy) DESC");

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
            logger.error("Error searching shelters: {}", e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Shelter save(Shelter shelter) {
        String sql = "INSERT INTO shelters (name, address, city, contact_person, contact_phone, " +
                     "total_capacity, current_occupancy, has_food, has_water, has_sanitation, " +
                     "has_medical, has_electricity, has_bedding, status, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, shelter.getName());
            ps.setString(2, shelter.getAddress());
            ps.setString(3, shelter.getCity());
            ps.setString(4, shelter.getContactPerson());
            ps.setString(5, shelter.getContactPhone());
            ps.setInt(6, shelter.getTotalCapacity());
            ps.setInt(7, shelter.getCurrentOccupancy());
            ps.setBoolean(8, shelter.isHasFood());
            ps.setBoolean(9, shelter.isHasWater());
            ps.setBoolean(10, shelter.isHasSanitation());
            ps.setBoolean(11, shelter.isHasMedical());
            ps.setBoolean(12, shelter.isHasElectricity());
            ps.setBoolean(13, shelter.isHasBedding());
            ps.setString(14, shelter.getStatus().name());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        shelter.setId(rs.getLong(1));
                    }
                }
            }
            return shelter;
        } catch (SQLException e) {
            logger.error("Error saving shelter: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to persist shelter: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Shelter shelter) {
        String sql = "UPDATE shelters SET name = ?, address = ?, city = ?, contact_person = ?, contact_phone = ?, " +
                     "total_capacity = ?, has_food = ?, has_water = ?, has_sanitation = ?, has_medical = ?, " +
                     "has_electricity = ?, has_bedding = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, shelter.getName());
            ps.setString(2, shelter.getAddress());
            ps.setString(3, shelter.getCity());
            ps.setString(4, shelter.getContactPerson());
            ps.setString(5, shelter.getContactPhone());
            ps.setInt(6, shelter.getTotalCapacity());
            ps.setBoolean(7, shelter.isHasFood());
            ps.setBoolean(8, shelter.isHasWater());
            ps.setBoolean(9, shelter.isHasSanitation());
            ps.setBoolean(10, shelter.isHasMedical());
            ps.setBoolean(11, shelter.isHasElectricity());
            ps.setBoolean(12, shelter.isHasBedding());
            ps.setString(13, shelter.getStatus().name());
            ps.setLong(14, shelter.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating shelter {}: {}", shelter.getId(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * Transactionally checks in evacuees, preventing over-allocation.
     */
    public boolean checkInEvacuees(Long shelterId, String repName, String phone, int peopleCount, String notes) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // Lock and inspect current capacity
            String checkSql = "SELECT total_capacity, current_occupancy, status FROM shelters WHERE id = ? FOR UPDATE";
            int total = 0, current = 0;
            String status = "";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setLong(1, shelterId);
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        total = rs.getInt("total_capacity");
                        current = rs.getInt("current_occupancy");
                        status = rs.getString("status");
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            if ("INACTIVE".equalsIgnoreCase(status) || (current + peopleCount) > total) {
                conn.rollback();
                return false; // Prevent over-allocation
            }

            int newOccupancy = current + peopleCount;
            String newStatus = (newOccupancy >= total) ? "FULL" : "ACTIVE";

            // Update shelter occupancy
            String updateSql = "UPDATE shelters SET current_occupancy = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                updatePs.setInt(1, newOccupancy);
                updatePs.setString(2, newStatus);
                updatePs.setLong(3, shelterId);
                updatePs.executeUpdate();
            }

            // Record allocation
            String allocSql = "INSERT INTO shelter_allocations (shelter_id, representative_name, contact_phone, " +
                              "people_count, check_in_date, status, notes) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, 'CHECKED_IN', ?)";
            try (PreparedStatement allocPs = conn.prepareStatement(allocSql)) {
                allocPs.setLong(1, shelterId);
                allocPs.setString(2, repName);
                allocPs.setString(3, phone);
                allocPs.setInt(4, peopleCount);
                allocPs.setString(5, notes);
                allocPs.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            logger.error("Error checking in evacuees to shelter {}: {}", shelterId, e.getMessage(), e);
            rollbackQuietly(conn);
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    /**
     * Transactionally releases evacuees and updates available capacity.
     */
    public boolean checkOutEvacuees(Long allocationId) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            Long shelterId = null;
            int peopleCount = 0;
            String currentStatus = "";

            String findAllocSql = "SELECT shelter_id, people_count, status FROM shelter_allocations WHERE id = ? FOR UPDATE";
            try (PreparedStatement ps = conn.prepareStatement(findAllocSql)) {
                ps.setLong(1, allocationId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        shelterId = rs.getLong("shelter_id");
                        peopleCount = rs.getInt("people_count");
                        currentStatus = rs.getString("status");
                    }
                }
            }

            if (shelterId == null || !"CHECKED_IN".equalsIgnoreCase(currentStatus)) {
                conn.rollback();
                return false;
            }

            // Update allocation record
            String updateAllocSql = "UPDATE shelter_allocations SET status = 'CHECKED_OUT', check_out_date = CURRENT_TIMESTAMP WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateAllocSql)) {
                ps.setLong(1, allocationId);
                ps.executeUpdate();
            }

            // Update shelter occupancy
            String updateShelterSql = "UPDATE shelters SET current_occupancy = GREATEST(0, current_occupancy - ?), " +
                                      "status = CASE WHEN status = 'FULL' THEN 'ACTIVE' ELSE status END, " +
                                      "updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateShelterSql)) {
                ps.setInt(1, peopleCount);
                ps.setLong(2, shelterId);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            logger.error("Error checking out allocation {}: {}", allocationId, e.getMessage(), e);
            rollbackQuietly(conn);
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public List<ShelterAllocation> findAllocationsByShelter(Long shelterId) {
        List<ShelterAllocation> list = new ArrayList<>();
        String sql = "SELECT sa.*, s.name as shelter_name FROM shelter_allocations sa " +
                     "JOIN shelters s ON sa.shelter_id = s.id " +
                     "WHERE sa.shelter_id = ? ORDER BY sa.check_in_date DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, shelterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ShelterAllocation sa = new ShelterAllocation();
                    sa.setId(rs.getLong("id"));
                    sa.setShelterId(rs.getLong("shelter_id"));
                    sa.setShelterName(rs.getString("shelter_name"));
                    sa.setRepresentativeName(rs.getString("representative_name"));
                    sa.setContactPhone(rs.getString("contact_phone"));
                    sa.setPeopleCount(rs.getInt("people_count"));
                    sa.setCheckInDate(rs.getTimestamp("check_in_date"));
                    sa.setCheckOutDate(rs.getTimestamp("check_out_date"));
                    sa.setStatus(rs.getString("status"));
                    sa.setNotes(rs.getString("notes"));
                    list.add(sa);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding allocations for shelter {}: {}", shelterId, e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM shelters WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting shelter {}: {}", id, e.getMessage(), e);
            return false;
        }
    }
}

