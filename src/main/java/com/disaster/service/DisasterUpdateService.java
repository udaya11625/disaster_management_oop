package com.disaster.service;

import com.disaster.dao.DisasterUpdateDAO;
import com.disaster.model.DisasterUpdate;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer for Official Disaster Advisories and Public Broadcasts.
 */
public class DisasterUpdateService {
    private final DisasterUpdateDAO updateDAO;

    public DisasterUpdateService() {
        this.updateDAO = new DisasterUpdateDAO();
    }

    public DisasterUpdateService(DisasterUpdateDAO updateDAO) {
        this.updateDAO = updateDAO;
    }

    public List<DisasterUpdate> getAllUpdates() {
        return updateDAO.findAll();
    }

    public List<DisasterUpdate> getActiveUpdates() {
        return updateDAO.findActiveUpdates();
    }

    public Optional<DisasterUpdate> getUpdateById(Long id) {
        return updateDAO.findById(id);
    }

    public DisasterUpdate createUpdate(DisasterUpdate update) {
        return updateDAO.save(update);
    }

    public boolean updateNotice(DisasterUpdate update) {
        return updateDAO.update(update);
    }

    public boolean deleteUpdate(Long id) {
        return updateDAO.deleteById(id);
    }
}

