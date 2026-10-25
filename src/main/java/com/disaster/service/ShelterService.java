package com.disaster.service;

import com.disaster.dao.ShelterDAO;
import com.disaster.model.Shelter;
import com.disaster.model.ShelterAllocation;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer for Shelter Management and Evacuee Allocation.
 */
public class ShelterService {
    private final ShelterDAO shelterDAO;

    public ShelterService() {
        this.shelterDAO = new ShelterDAO();
    }

    public ShelterService(ShelterDAO shelterDAO) {
        this.shelterDAO = shelterDAO;
    }

    public List<Shelter> getAllShelters() {
        return shelterDAO.findAll();
    }

    public List<Shelter> getActiveShelters() {
        return shelterDAO.findActiveShelters();
    }

    public Optional<Shelter> getShelterById(Long id) {
        return shelterDAO.findById(id);
    }

    public List<Shelter> searchShelters(String keyword, String city, Boolean medicalOnly) {
        return shelterDAO.searchShelters(keyword, city, medicalOnly);
    }

    public Shelter addShelter(Shelter shelter) {
        return shelterDAO.save(shelter);
    }

    public boolean updateShelter(Shelter shelter) {
        return shelterDAO.update(shelter);
    }

    public boolean checkInEvacuees(Long shelterId, String repName, String phone, int peopleCount, String notes) {
        return shelterDAO.checkInEvacuees(shelterId, repName, phone, peopleCount, notes);
    }

    public boolean checkOutEvacuees(Long allocationId) {
        return shelterDAO.checkOutEvacuees(allocationId);
    }

    public List<ShelterAllocation> getShelterAllocations(Long shelterId) {
        return shelterDAO.findAllocationsByShelter(shelterId);
    }

    public boolean deleteShelter(Long id) {
        return shelterDAO.deleteById(id);
    }
}

