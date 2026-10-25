package com.disaster.web.servlet;

import com.disaster.model.AssistanceRequest;
import com.disaster.model.RescueTeam;
import com.disaster.model.User;
import com.disaster.model.enums.DisasterType;
import com.disaster.model.enums.RequestStatus;
import com.disaster.model.enums.Severity;
import com.disaster.service.AssistanceRequestService;
import com.disaster.service.UserService;
import com.disaster.web.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for Emergency SOS Assistance Requests.
 */
@WebServlet(name = "AssistanceRequestServlet", urlPatterns = {"/request-assistance", "/admin/requests/assign", "/admin/requests/status"})
public class AssistanceRequestServlet extends HttpServlet {
    private final AssistanceRequestService requestService = new AssistanceRequestService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/request-assistance".equals(path)) {
            HttpSession session = req.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

            req.setAttribute("disasterTypes", DisasterType.values());
            req.setAttribute("severities", Severity.values());
            if (currentUser != null) {
                req.setAttribute("defaultName", currentUser.getFullName());
                req.setAttribute("defaultPhone", currentUser.getPhone());
                req.setAttribute("defaultEmail", currentUser.getEmail());
                req.setAttribute("defaultAddress", currentUser.getAddress());
            }
            req.getRequestDispatcher("/WEB-INF/views/public/request-assistance.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/request-assistance".equals(path)) {
            handleCreateRequest(req, resp);
        } else if ("/admin/requests/assign".equals(path)) {
            handleAssignTeam(req, resp);
        } else if ("/admin/requests/status".equals(path)) {
            handleUpdateStatus(req, resp);
        }
    }

    private void handleCreateRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        String contactName = req.getParameter("contactName");
        String contactPhone = req.getParameter("contactPhone");
        String contactEmail = req.getParameter("contactEmail");
        String disasterTypeStr = req.getParameter("disasterType");
        String location = req.getParameter("location");
        String landmark = req.getParameter("landmark");
        String peopleAffectedStr = req.getParameter("peopleAffected");
        String assistanceType = req.getParameter("assistanceType");
        String priorityStr = req.getParameter("priority");
        String specialNeeds = req.getParameter("specialNeeds");

        if (!ValidationUtils.isNotEmpty(contactName) || !ValidationUtils.isNotEmpty(contactPhone) ||
            !ValidationUtils.isNotEmpty(location) || !ValidationUtils.isNotEmpty(assistanceType)) {
            req.setAttribute("errorMessage", "Please provide all required emergency contact and location details.");
            req.setAttribute("disasterTypes", DisasterType.values());
            req.setAttribute("severities", Severity.values());
            req.getRequestDispatcher("/WEB-INF/views/public/request-assistance.jsp").forward(req, resp);
            return;
        }

        int peopleAffected = ValidationUtils.parsePositiveInt(peopleAffectedStr, 1);

        AssistanceRequest request = new AssistanceRequest();
        request.setContactName(contactName.trim());
        request.setContactPhone(contactPhone.trim());
        request.setContactEmail(contactEmail != null ? contactEmail.trim() : "");
        request.setDisasterType(DisasterType.fromString(disasterTypeStr));
        request.setLocation(location.trim());
        request.setLandmark(landmark != null ? landmark.trim() : "");
        request.setPeopleAffected(peopleAffected);
        request.setAssistanceType(assistanceType);
        request.setSeverity(Severity.fromString(priorityStr));
        request.setSpecialNeeds(specialNeeds != null ? specialNeeds.trim() : "");
        request.setStatus(RequestStatus.PENDING);

        if (currentUser != null) {
            request.setCitizenId(currentUser.getId());
        }

        AssistanceRequest saved = requestService.submitRequest(request);
        session.setAttribute("successMessage", "EMERGENCY REQUEST LOGGED! Tracking Code: " + saved.getCode() + 
                             ". Our Emergency Operation Center has prioritized your dispatch.");

        if (currentUser != null) {
            resp.sendRedirect(req.getContextPath() + "/citizen/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }

    private void handleAssignTeam(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null || !currentUser.hasAdministrativePrivileges()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long requestId = Long.parseLong(req.getParameter("requestId"));
        Long teamId = Long.parseLong(req.getParameter("teamId"));

        requestService.assignRescueTeam(requestId, teamId);
        session.setAttribute("successMessage", "Rescue Squad dispatched and assigned to emergency request.");
        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
    }

    private void handleUpdateStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long requestId = Long.parseLong(req.getParameter("requestId"));
        RequestStatus status = RequestStatus.fromString(req.getParameter("status"));
        String notes = req.getParameter("resolutionNotes");

        requestService.updateRequestStatus(requestId, status, notes);
        session.setAttribute("successMessage", "Assistance request updated to: " + status.getDisplayName());

        if (currentUser.hasAdministrativePrivileges()) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + currentUser.getDashboardUrl());
        }
    }
}

