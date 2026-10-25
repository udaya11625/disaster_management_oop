package com.disaster.service;

import com.disaster.dao.RescueTeamDAO;
import com.disaster.dao.UserDAO;
import com.disaster.model.*;
import com.disaster.model.enums.Role;
import com.disaster.web.util.PasswordUtils;
import com.disaster.web.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer for User Authentication, Registration, and Profile Management.
 * Implements strict role boundary enforcement.
 */
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO;
    private final RescueTeamDAO rescueTeamDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        this.rescueTeamDAO = new RescueTeamDAO();
    }

    public UserService(UserDAO userDAO, RescueTeamDAO rescueTeamDAO) {
        this.userDAO = userDAO;
        this.rescueTeamDAO = rescueTeamDAO;
    }

    /**
     * Authenticates a user by username and plain password using BCrypt.
     */
    public Optional<User> authenticate(String username, String plainPassword) {
        if (!ValidationUtils.isNotEmpty(username) || !ValidationUtils.isNotEmpty(plainPassword)) {
            return Optional.empty();
        }

        Optional<User> userOpt = userDAO.findByUsername(username.trim());
        if (userOpt.isEmpty()) {
            // Also try email login
            userOpt = userDAO.findByEmail(username.trim());
        }

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isActive() && PasswordUtils.checkPassword(plainPassword, user.getPasswordHash())) {
                logger.info("Authentication successful for user: {} (Role: {})", user.getUsername(), user.getRole());
                return Optional.of(user);
            }
        }

        logger.warn("Authentication failed for username/email attempt: {}", username);
        return Optional.empty();
    }

    /**
     * Registers a new user.
     * Enforces security policy: ADMIN role can NEVER be registered via public form.
     */
    public User registerUser(String username, String email, String password,
                             String fullName, String phone, String address, Role requestedRole) {
        // Enforce administrative lockdown
        if (requestedRole == Role.ADMIN) {
            logger.warn("Security Alert: Attempted unauthorized self-registration as ADMIN for username: {}", username);
            throw new SecurityException("Administrator accounts cannot be created via public registration. Contact your system director.");
        }

        // Validate uniqueness
        if (userDAO.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username '" + username + "' is already registered.");
        }
        if (userDAO.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email address '" + email + "' is already in use.");
        }

        String hashedPassword = PasswordUtils.hashPassword(password);
        Role role = (requestedRole != null) ? requestedRole : Role.CITIZEN;

        User newUser;
        if (role == Role.VOLUNTEER) {
            newUser = new Volunteer(null, username, email, hashedPassword, fullName, phone, address);
        } else if (role == Role.RESCUE_TEAM) {
            newUser = new RescueTeamMember(null, username, email, hashedPassword, fullName, phone, address);
        } else {
            newUser = new Citizen(null, username, email, hashedPassword, fullName, phone, address);
        }

        return userDAO.save(newUser);
    }

    /**
     * Registers a volunteer with detailed skill profiling.
     */
    public Volunteer registerVolunteer(Volunteer volunteer, String plainPassword) {
        if (userDAO.findByUsername(volunteer.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username '" + volunteer.getUsername() + "' is already registered.");
        }
        if (userDAO.findByEmail(volunteer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email address '" + volunteer.getEmail() + "' is already in use.");
        }

        volunteer.setPasswordHash(PasswordUtils.hashPassword(plainPassword));
        volunteer.setRole(Role.VOLUNTEER);
        return (Volunteer) userDAO.save(volunteer);
    }

    public Optional<User> getUserById(Long id) {
        return userDAO.findById(id);
    }

    public boolean updateProfile(User user) {
        return userDAO.update(user);
    }

    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userDAO.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (PasswordUtils.checkPassword(oldPassword, user.getPasswordHash())) {
                String newHash = PasswordUtils.hashPassword(newPassword);
                return userDAO.updatePassword(userId, newHash);
            }
        }
        return false;
    }

    public List<Volunteer> getAllVolunteers() {
        return userDAO.findAllVolunteers();
    }

    public List<RescueTeam> getAllRescueTeams() {
        return rescueTeamDAO.findAll();
    }

    public List<RescueTeam> getAvailableRescueTeams() {
        return rescueTeamDAO.findAvailableTeams();
    }
}

