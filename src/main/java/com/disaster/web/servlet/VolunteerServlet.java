package com.disaster.web.servlet;

import com.disaster.model.IncidentAssignment;
import com.disaster.model.User;
import com.disaster.model.Volunteer;
import com.disaster.model.enums.AvailabilityStatus;
import com.disaster.model.enums.Role;
import com.disaster.service.UserService;
import com.disaster.service.VolunteerService;
import com.disaster.web.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for Volunteer Registration and Volunteer Task Coordination.
 */
@WebServlet(name = "VolunteerServlet", urlPatterns = {
    "/volunteer-register", 
    "/volunteer/tasks/update", 
    "/admin/volunteers/assign"
})
public class VolunteerServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final VolunteerService volunteerService = new VolunteerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/volunteer-register".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/public/volunteer-register.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/volunteer-register".equals(path)) {
            handleVolunteerRegistration(req, resp);
        } else if ("/volunteer/tasks/update".equals(path)) {
            handleTaskUpdate(req, resp);
        } else if ("/admin/volunteers/assign".equals(path)) {
            handleAdminAssignVolunteer(req, resp);
        }
    }

    private void handleVolunteerRegistration(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String skills = req.getParameter("skills");
        String preferredAssignments = req.getParameter("preferredAssignments");
        int experienceYears = ValidationUtils.parsePositiveInt(req.getParameter("experienceYears"), 0);
        String location = req.getParameter("location");

        if (!ValidationUtils.isNotEmpty(username) || !ValidationUtils.isNotEmpty(email) ||
            !ValidationUtils.isNotEmpty(password) || !ValidationUtils.isNotEmpty(fullName) ||
            !ValidationUtils.isNotEmpty(phone) || !ValidationUtils.isNotEmpty(skills)) {
            req.setAttribute("errorMessage", "Please complete all required fields including skills.");
            req.getRequestDispatcher("/WEB-INF/views/public/volunteer-register.jsp").forward(req, resp);
            return;
        }

        try {
            Volunteer vol = new Volunteer(null, username.trim(), email.trim(), null, fullName.trim(), phone.trim(), address != null ? address.trim() : "");
            vol.setSkills(skills.trim());
            vol.setPreferredAssignments(preferredAssignments != null ? preferredAssignments.trim() : "Emergency First Response");
            vol.setExperienceYears(experienceYears);
            vol.setLocation(location != null ? location.trim() : "Metro Area");
            vol.setAvailability(AvailabilityStatus.AVAILABLE);

            Volunteer saved = userService.registerVolunteer(vol, password);
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", saved);
            session.setAttribute("userRole", Role.VOLUNTEER.name());
            session.setAttribute("userName", saved.getFullName());
            session.setAttribute("successMessage", "Welcome to the Volunteer Response Corps! Your profile is active and ready for dispatch.");
            resp.sendRedirect(req.getContextPath() + "/volunteer/dashboard");
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/public/volunteer-register.jsp").forward(req, resp);
        }
    }

    private void handleTaskUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long assignmentId = Long.parseLong(req.getParameter("assignmentId"));
        String status = req.getParameter("status"); // IN_PROGRESS, COMPLETED

        volunteerService.updateAssignmentStatus(assignmentId, status);
        session.setAttribute("successMessage", "Task status updated to: " + status);
        resp.sendRedirect(req.getContextPath() + "/volunteer/dashboard");
    }

    private void handleAdminAssignVolunteer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null || !currentUser.hasAdministrativePrivileges()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long volunteerId = Long.parseLong(req.getParameter("volunteerId"));
        String reportIdStr = req.getParameter("reportId");
        String requestIdStr = req.getParameter("requestId");
        String taskTitle = req.getParameter("taskTitle");
        String taskDescription = req.getParameter("taskDescription");

        IncidentAssignment assignment = new IncidentAssignment();
        assignment.setVolunteerId(volunteerId);
        if (ValidationUtils.isNotEmpty(reportIdStr)) assignment.setReportId(Long.parseLong(reportIdStr));
        if (ValidationUtils.isNotEmpty(requestIdStr)) assignment.setRequestId(Long.parseLong(requestIdStr));
        assignment.setTaskTitle(taskTitle);
        assignment.setTaskDescription(taskDescription);
        assignment.setAssignedBy(currentUser.getId());
        assignment.setStatus("ASSIGNED");

        volunteerService.assignTask(assignment);
        session.setAttribute("successMessage", "Volunteer successfully assigned to disaster task.");
        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
    }
}

