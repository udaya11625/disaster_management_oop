package com.disaster.service;

import com.disaster.dao.ReliefResourceDAO;
import com.disaster.model.ReliefResource;
import com.disaster.model.ResourceDistribution;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer for Relief Supplies Inventory and Distribution Logistics.
 */
public class ReliefResourceService {
    private final ReliefResourceDAO resourceDAO;

    public ReliefResourceService() {
        this.resourceDAO = new ReliefResourceDAO();
    }

    public ReliefResourceService(ReliefResourceDAO resourceDAO) {
        this.resourceDAO = resourceDAO;
    }

    public List<ReliefResource> getAllResources() {
        return resourceDAO.findAll();
    }

    public List<ReliefResource> getLowStockResources() {
        return resourceDAO.findLowStockResources();
    }

    public Optional<ReliefResource> getResourceById(Long id) {
        return resourceDAO.findById(id);
    }

    public List<ReliefResource> searchResources(String keyword, String category) {
        return resourceDAO.searchResources(keyword, category);
    }

    public ReliefResource addResource(ReliefResource resource) {
        return resourceDAO.save(resource);
    }

    public boolean updateResource(ReliefResource resource) {
        return resourceDAO.update(resource);
    }

    public boolean replenishStock(Long resourceId, int quantity) {
        return resourceDAO.replenishStock(resourceId, quantity);
    }

    public boolean distributeResource(Long resourceId, int quantity, String destType, String destName,
                                      Long shelterId, Long requestId, Long distributedBy, String contact, String remarks) {
        return resourceDAO.recordDistribution(resourceId, quantity, destType, destName, shelterId, requestId, distributedBy, contact, remarks);
    }

    public List<ResourceDistribution> getAllDistributions() {
        return resourceDAO.getAllDistributions();
    }

    public boolean deleteResource(Long id) {
        return resourceDAO.deleteById(id);
    }
}

