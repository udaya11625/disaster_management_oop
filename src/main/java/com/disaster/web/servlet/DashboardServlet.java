package com.disaster.web.servlet;

import com.disaster.dao.DashboardDAO;
import com.disaster.model.*;
import com.disaster.model.enums.ReportStatus;
import com.disaster.model.enums.RequestStatus;
import com.disaster.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller routing to Role-Specific Operational Dashboards.
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {
    "/citizen/dashboard", 
    "/volunteer/dashboard", 
    "/rescue/dashboard", 
    "/admin/dashboard"
})
public class DashboardServlet extends HttpServlet {
    private final DisasterReportService reportService = new DisasterReportService();
    private final AssistanceRequestService requestService = new AssistanceRequestService();
    private final ShelterService shelterService = new ShelterService();
    private final ReliefResourceService resourceService = new ReliefResourceService();
    private final VolunteerService volunteerService = new VolunteerService();
    private final UserService userService = new UserService();
    private final DisasterUpdateService updateService = new DisasterUpdateService();
    private final DashboardDAO dashboardDAO = new DashboardDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("/citizen/dashboard".equals(path)) {
            renderCitizenDashboard(req, resp, currentUser);
        } else if ("/volunteer/dashboard".equals(path)) {
            renderVolunteerDashboard(req, resp, currentUser);
        } else if ("/rescue/dashboard".equals(path)) {
            renderRescueDashboard(req, resp, currentUser);
        } else if ("/admin/dashboard".equals(path)) {
            renderAdminDashboard(req, resp);
        }
    }

    private void renderCitizenDashboard(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        List<DisasterReport> myReports = reportService.getReportsByReporter(user.getId());
        List<AssistanceRequest> myRequests = requestService.getRequestsByCitizen(user.getId());
        List<Shelter> nearbyShelters = shelterService.getActiveShelters();
        if (nearbyShelters.size() > 4) nearbyShelters = nearbyShelters.subList(0, 4);

        req.setAttribute("myReports", myReports);
        req.setAttribute("myRequests", myRequests);
        req.setAttribute("nearbyShelters", nearbyShelters);
        req.getRequestDispatcher("/WEB-INF/views/citizen/dashboard.jsp").forward(req, resp);
    }

    private void renderVolunteerDashboard(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        List<IncidentAssignment> myAssignments = volunteerService.getAssignmentsByVolunteer(user.getId());
        List<DisasterUpdate> activeAdvisories = updateService.getActiveUpdates();

        req.setAttribute("myAssignments", myAssignments);
        req.setAttribute("activeAdvisories", activeAdvisories);
        req.getRequestDispatcher("/WEB-INF/views/volunteer/dashboard.jsp").forward(req, resp);
    }

    private void renderRescueDashboard(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        Long teamId = 1L; // default squad or matched from user
        if (user instanceof RescueTeamMember rtm && rtm.getTeamId() != null) {
            teamId = rtm.getTeamId();
        }

        List<AssistanceRequest> assignedMissions = requestService.getRequestsByRescueTeam(teamId);
        List<RescueTeam> allTeams = userService.getAllRescueTeams();

        RescueTeam currentTeam = null;
        for (RescueTeam t : allTeams) {
            if (t.getId().equals(teamId)) {
                currentTeam = t;
                break;
            }
        }
        if (currentTeam == null && !allTeams.isEmpty()) {
            currentTeam = allTeams.get(0);
        }

        req.setAttribute("currentTeam", currentTeam);
        req.setAttribute("assignedMissions", assignedMissions);
        req.setAttribute("requestStatuses", RequestStatus.values());
        req.getRequestDispatcher("/WEB-INF/views/rescue/dashboard.jsp").forward(req, resp);
    }

    private void renderAdminDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> stats = dashboardDAO.getAdminDashboardStats();
        List<DisasterReport> allReports = reportService.getAllReports();
        List<AssistanceRequest> allRequests = requestService.getAllRequests();
        List<RescueTeam> rescueTeams = userService.getAllRescueTeams();
        List<Volunteer> volunteers = userService.getAllVolunteers();
        List<ReliefResource> lowStock = resourceService.getLowStockResources();
        List<Map<String, Object>> recentActivities = dashboardDAO.getRecentActivity();

        req.setAttribute("stats", stats);
        req.setAttribute("reports", allReports);
        req.setAttribute("requests", allRequests);
        req.setAttribute("rescueTeams", rescueTeams);
        req.setAttribute("volunteers", volunteers);
        req.setAttribute("lowStock", lowStock);
        req.setAttribute("recentActivities", recentActivities);
        req.setAttribute("reportStatuses", ReportStatus.values());
        req.setAttribute("requestStatuses", RequestStatus.values());

        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }
}

