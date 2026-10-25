package com.disaster.model;

import com.disaster.model.enums.Role;

/**
 * Citizen domain model extending User.
 */
public class Citizen extends User {
    private static final long serialVersionUID = 1L;

    public Citizen() {
        super();
        setRole(Role.CITIZEN);
    }

    public Citizen(Long id, String username, String email, String passwordHash, 
                   String fullName, String phone, String address) {
        super(id, username, email, passwordHash, fullName, phone, address, Role.CITIZEN);
    }

    @Override
    public String getDashboardUrl() {
        return "/citizen/dashboard";
    }

    @Override
    public boolean hasAdministrativePrivileges() {
        return false;
    }

    @Override
    public String getRoleBadge() {
        return "<span class=\"badge bg-info text-dark\"><i class=\"bi bi-person me-1\"></i>Citizen</span>";
    }
}

