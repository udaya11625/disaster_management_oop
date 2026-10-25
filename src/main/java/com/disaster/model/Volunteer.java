package com.disaster.model;

import com.disaster.model.enums.AvailabilityStatus;
import com.disaster.model.enums.Role;

/**
 * Volunteer domain model extending User.
 * Encapsulates volunteer-specific skill set, experience, and availability.
 */
public class Volunteer extends User {
    private static final long serialVersionUID = 1L;

    private String skills;
    private AvailabilityStatus availability = AvailabilityStatus.AVAILABLE;
    private String preferredAssignments;
    private int experienceYears;
    private String location;

    public Volunteer() {
        super();
        setRole(Role.VOLUNTEER);
    }

    public Volunteer(Long id, String username, String email, String passwordHash, 
                     String fullName, String phone, String address) {
        super(id, username, email, passwordHash, fullName, phone, address, Role.VOLUNTEER);
    }

    @Override
    public String getDashboardUrl() {
        return "/volunteer/dashboard";
    }

    @Override
    public boolean hasAdministrativePrivileges() {
        return false;
    }

    @Override
    public String getRoleBadge() {
        return "<span class=\"badge bg-success\"><i class=\"bi bi-heart me-1\"></i>Volunteer</span>";
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public AvailabilityStatus getAvailability() {
        return availability;
    }

    public void setAvailability(AvailabilityStatus availability) {
        this.availability = availability;
    }

    public String getPreferredAssignments() {
        return preferredAssignments;
    }

    public void setPreferredAssignments(String preferredAssignments) {
        this.preferredAssignments = preferredAssignments;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAvailable() {
        return availability == AvailabilityStatus.AVAILABLE;
    }
}

