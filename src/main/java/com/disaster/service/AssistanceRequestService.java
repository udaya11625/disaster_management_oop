package com.disaster.service;

import com.disaster.dao.AssistanceRequestDAO;
import com.disaster.model.AssistanceRequest;
import com.disaster.model.enums.RequestStatus;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Service Layer for Emergency SOS & Assistance Requests.
 */
public class AssistanceRequestService {
    private final AssistanceRequestDAO requestDAO;

    public AssistanceRequestService() {
        this.requestDAO = new AssistanceRequestDAO();
    }

    public AssistanceRequestService(AssistanceRequestDAO requestDAO) {
        this.requestDAO = requestDAO;
    }

    public AssistanceRequest submitRequest(AssistanceRequest request) {
        if (request.getCode() == null || request.getCode().isEmpty()) {
            request.setCode(generateRequestCode());
        }
        if (request.getStatus() == null) {
            request.setStatus(RequestStatus.PENDING);
        }
        return requestDAO.save(request);
    }

    public List<AssistanceRequest> getAllRequests() {
        return requestDAO.findAll();
    }

    public Optional<AssistanceRequest> getRequestById(Long id) {
        return requestDAO.findById(id);
    }

    public Optional<AssistanceRequest> getRequestByCode(String code) {
        return requestDAO.findByCode(code);
    }

    public List<AssistanceRequest> getRequestsByCitizen(Long citizenId) {
        return requestDAO.findByCitizenId(citizenId);
    }

    public List<AssistanceRequest> getRequestsByRescueTeam(Long teamId) {
        return requestDAO.findByAssignedTeamId(teamId);
    }

    public List<AssistanceRequest> searchRequests(String keyword, String type, String priority, String status) {
        return requestDAO.searchRequests(keyword, type, priority, status);
    }

    public boolean assignRescueTeam(Long requestId, Long teamId) {
        return requestDAO.assignTeam(requestId, teamId);
    }

    public boolean updateRequestStatus(Long requestId, RequestStatus status, String resolutionNotes) {
        return requestDAO.updateStatus(requestId, status, resolutionNotes);
    }

    public boolean updateRequest(AssistanceRequest req) {
        return requestDAO.update(req);
    }

    public boolean deleteRequest(Long id) {
        return requestDAO.deleteById(id);
    }

    private String generateRequestCode() {
        int year = Year.now().getValue();
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "REQ-" + year + "-" + random;
    }
}

