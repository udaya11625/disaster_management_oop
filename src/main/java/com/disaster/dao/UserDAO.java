package com.disaster.dao;

import com.disaster.model.*;
import com.disaster.model.enums.AvailabilityStatus;
import com.disaster.model.enums.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User Data Access Object.
 * Demonstrates POLYMORPHIC ENTITY CREATION based on the user's role.
 */
public class UserDAO extends AbstractDAO<User> implements GenericDAO<User, Long> {

    @Override
    protected User mapResultSetToEntity(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String passwordHash = rs.getString("password_hash");
        String fullName = rs.getString("full_name");
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        Role role = Role.fromString(rs.getString("role"));
        String status = rs.getString("status");
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        User user;
        switch (role) {
            case VOLUNTEER:
                Volunteer vol = new Volunteer(id, username, email, passwordHash, fullName, phone, address);
                user = vol;
                break;
            case RESCUE_TEAM:
                RescueTeamMember rescue = new RescueTeamMember(id, username, email, passwordHash, fullName, phone, address);
                user = rescue;
                break;
            case ADMIN:
                user = new Admin(id, username, email, passwordHash, fullName, phone, address);
                break;
            case CITIZEN:
            default:
                user = new Citizen(id, username, email, passwordHash, fullName, phone, address);
                break;
        }

        user.setStatus(status);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToEntity(rs);
                    populateSpecializedDetails(conn, user);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user by id {}: {}", id, e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE LOWER(username) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToEntity(rs);
                    populateSpecializedDetails(conn, user);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user by username {}: {}", username, e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapResultSetToEntity(rs);
                    populateSpecializedDetails(conn, user);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user by email {}: {}", email, e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all users: {}", e.getMessage(), e);
        }
        return users;
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (username, email, password_hash, full_name, phone, address, role, status, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getRole().name());
            ps.setString(8, user.getStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getLong(1));
                    }
                }

                // If Volunteer, insert volunteer profile
                if (user instanceof Volunteer vol) {
                    saveVolunteerProfile(conn, vol);
                }
            }
            return user;
        } catch (SQLException e) {
            logger.error("Error saving user {}: {}", user.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Failed to persist user: " + e.getMessage(), e);
        }
    }

    public void saveVolunteerProfile(Connection conn, Volunteer vol) {
        String sql = "INSERT INTO volunteer_profiles (user_id, skills, availability, preferred_assignments, experience_years, location) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, vol.getId());
            ps.setString(2, vol.getSkills());
            ps.setString(3, vol.getAvailability() != null ? vol.getAvailability().name() : "AVAILABLE");
            ps.setString(4, vol.getPreferredAssignments());
            ps.setInt(5, vol.getExperienceYears());
            ps.setString(6, vol.getLocation());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Could not save volunteer profile for user id {}: {}", vol.getId(), e.getMessage());
        }
    }

    public void updateVolunteerProfile(Volunteer vol) {
        String sql = "UPDATE volunteer_profiles SET skills = ?, availability = ?, preferred_assignments = ?, experience_years = ?, location = ? " +
                     "WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vol.getSkills());
            ps.setString(2, vol.getAvailability() != null ? vol.getAvailability().name() : "AVAILABLE");
            ps.setString(3, vol.getPreferredAssignments());
            ps.setInt(4, vol.getExperienceYears());
            ps.setString(5, vol.getLocation());
            ps.setLong(6, vol.getId());
            int affected = ps.executeUpdate();
            if (affected == 0) {
                saveVolunteerProfile(conn, vol);
            }
        } catch (SQLException e) {
            logger.error("Error updating volunteer profile: {}", e.getMessage(), e);
        }
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET full_name = ?, phone = ?, address = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getStatus());
            ps.setLong(5, user.getId());
            int affected = ps.executeUpdate();

            if (user instanceof Volunteer vol) {
                updateVolunteerProfile(vol);
            }
            return affected > 0;
        } catch (SQLException e) {
            logger.error("Error updating user {}: {}", user.getId(), e.getMessage(), e);
            return false;
        }
    }

    public boolean updatePassword(Long userId, String newPasswordHash) {
        String sql = "UPDATE users SET password_hash = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPasswordHash);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating password for user {}: {}", userId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error deleting user {}: {}", id, e.getMessage(), e);
            return false;
        }
    }

    public List<Volunteer> findAllVolunteers() {
        List<Volunteer> list = new ArrayList<>();
        String sql = "SELECT u.*, vp.skills, vp.availability, vp.preferred_assignments, vp.experience_years, vp.location " +
                     "FROM users u " +
                     "LEFT JOIN volunteer_profiles vp ON u.id = vp.user_id " +
                     "WHERE u.role = 'VOLUNTEER' ORDER BY u.full_name ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Volunteer vol = (Volunteer) mapResultSetToEntity(rs);
                vol.setSkills(rs.getString("skills"));
                vol.setAvailability(AvailabilityStatus.fromString(rs.getString("availability")));
                vol.setPreferredAssignments(rs.getString("preferred_assignments"));
                vol.setExperienceYears(rs.getInt("experience_years"));
                vol.setLocation(rs.getString("location"));
                list.add(vol);
            }
        } catch (SQLException e) {
            logger.error("Error listing volunteers: {}", e.getMessage(), e);
        }
        return list;
    }

    private void populateSpecializedDetails(Connection conn, User user) {
        if (user instanceof Volunteer vol) {
            String sql = "SELECT * FROM volunteer_profiles WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, vol.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        vol.setSkills(rs.getString("skills"));
                        vol.setAvailability(AvailabilityStatus.fromString(rs.getString("availability")));
                        vol.setPreferredAssignments(rs.getString("preferred_assignments"));
                        vol.setExperienceYears(rs.getInt("experience_years"));
                        vol.setLocation(rs.getString("location"));
                    }
                }
            } catch (SQLException e) {
                logger.warn("Could not load volunteer details for {}: {}", vol.getId(), e.getMessage());
            }
        } else if (user instanceof RescueTeamMember rescue) {
            String sql = "SELECT rtm.team_id, rtm.role_in_team, rt.team_name, rt.specialization " +
                         "FROM rescue_team_members rtm " +
                         "JOIN rescue_teams rt ON rtm.team_id = rt.id " +
                         "WHERE rtm.user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, rescue.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        rescue.setTeamId(rs.getLong("team_id"));
                        rescue.setTeamName(rs.getString("team_name"));
                        rescue.setSpecialization(rs.getString("specialization"));
                        rescue.setRoleInTeam(rs.getString("role_in_team"));
                    }
                }
            } catch (SQLException e) {
                logger.warn("Could not load rescue member details for {}: {}", rescue.getId(), e.getMessage());
            }
        }
    }
}

