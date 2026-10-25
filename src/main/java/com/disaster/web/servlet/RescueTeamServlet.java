package com.disaster.web.servlet;

import com.disaster.dao.RescueTeamDAO;
import com.disaster.model.RescueTeam;
import com.disaster.model.User;
import com.disaster.model.enums.AvailabilityStatus;
import com.disaster.web.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for Rescue Team Operations and Mission Readiness.
 */
@WebServlet(name = "RescueTeamServlet", urlPatterns = {"/rescue/mission/update", "/admin/rescue-teams/add"})
public class RescueTeamServlet extends HttpServlet {
    private final RescueTeamDAO rescueTeamDAO = new RescueTeamDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if ("/rescue/mission/update".equals(path)) {
            Long teamId = Long.parseLong(req.getParameter("teamId"));
            AvailabilityStatus status = AvailabilityStatus.fromString(req.getParameter("availability"));
            rescueTeamDAO.updateAvailability(teamId, status);
            session.setAttribute("successMessage", "Rescue Team readiness updated to: " + status.getDisplayName());
            resp.sendRedirect(req.getContextPath() + "/rescue/dashboard");
        } else if ("/admin/rescue-teams/add".equals(path)) {
            if (!currentUser.hasAdministrativePrivileges()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            String teamName = req.getParameter("teamName");
            String contactNumber = req.getParameter("contactNumber");
            String specialization = req.getParameter("specialization");
            int memberCount = ValidationUtils.parsePositiveInt(req.getParameter("memberCount"), 6);
            String operationalArea = req.getParameter("operationalArea");

            RescueTeam team = new RescueTeam();
            team.setTeamName(teamName);
            team.setContactNumber(contactNumber);
            team.setSpecialization(specialization);
            team.setMemberCount(memberCount);
            team.setOperationalArea(operationalArea);
            team.setAvailability(AvailabilityStatus.AVAILABLE);

            rescueTeamDAO.save(team);
            session.setAttribute("successMessage", "New Rescue Squad created: " + teamName);
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        }
    }
}

