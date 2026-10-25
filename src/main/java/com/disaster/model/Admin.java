package com.disaster.model;

import com.disaster.model.enums.Role;

/**
 * Administrator domain model extending User.
 * Has full platform administration authority.
 */
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin() {
        super();
        setRole(Role.ADMIN);
    }

    public Admin(Long id, String username, String email, String passwordHash, 
                 String fullName, String phone, String address) {
        super(id, username, email, passwordHash, fullName, phone, address, Role.ADMIN);
    }

    @Override
    public String getDashboardUrl() {
        return "/admin/dashboard";
    }

    @Override
    public boolean hasAdministrativePrivileges() {
        return true;
    }

    @Override
    public String getRoleBadge() {
        return "<span class=\"badge bg-danger\"><i class=\"bi bi-shield-lock me-1\"></i>Administrator</span>";
    }
}

