package com.disaster.service;

import com.disaster.dao.IncidentAssignmentDAO;
import com.disaster.model.IncidentAssignment;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer for Volunteer Task Assignments.
 */
public class VolunteerService {
    private final IncidentAssignmentDAO assignmentDAO;

    public VolunteerService() {
        this.assignmentDAO = new IncidentAssignmentDAO();
    }

    public IncidentAssignment assignTask(IncidentAssignment assignment) {
        return assignmentDAO.save(assignment);
    }

    public List<IncidentAssignment> getAssignmentsByVolunteer(Long volunteerId) {
        return assignmentDAO.findByVolunteerId(volunteerId);
    }

    public List<IncidentAssignment> getAllAssignments() {
        return assignmentDAO.findAll();
    }

    public Optional<IncidentAssignment> getAssignmentById(Long id) {
        return assignmentDAO.findById(id);
    }

    public boolean updateAssignmentStatus(Long assignmentId, String status) {
        return assignmentDAO.updateStatus(assignmentId, status);
    }
}

