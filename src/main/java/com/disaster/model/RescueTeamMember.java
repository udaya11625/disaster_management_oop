package com.disaster.model;

import com.disaster.model.enums.Role;

/**
 * RescueTeamMember domain model extending User.
 * Represents emergency rescue personnel associated with specialized rescue squads.
 */
public class RescueTeamMember extends User {
    private static final long serialVersionUID = 1L;

    private Long teamId;
    private String teamName;
    private String specialization;
    private String roleInTeam;

    public RescueTeamMember() {
        super();
        setRole(Role.RESCUE_TEAM);
    }

    public RescueTeamMember(Long id, String username, String email, String passwordHash, 
                            String fullName, String phone, String address) {
        super(id, username, email, passwordHash, fullName, phone, address, Role.RESCUE_TEAM);
    }

    @Override
    public String getDashboardUrl() {
        return "/rescue/dashboard";
    }

    @Override
    public boolean hasAdministrativePrivileges() {
        return false;
    }

    @Override
    public String getRoleBadge() {
        return "<span class=\"badge bg-primary\"><i class=\"bi bi-shield-shaded me-1\"></i>Rescue Team</span>";
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getRoleInTeam() {
        return roleInTeam;
    }

    public void setRoleInTeam(String roleInTeam) {
        this.roleInTeam = roleInTeam;
    }
}

