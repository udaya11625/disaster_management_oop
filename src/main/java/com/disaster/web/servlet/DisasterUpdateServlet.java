package com.disaster.web.servlet;

import com.disaster.model.DisasterUpdate;
import com.disaster.model.User;
import com.disaster.model.enums.DisasterType;
import com.disaster.service.DisasterUpdateService;
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
 * Controller for Official Disaster Updates and Emergency Advisories.
 */
@WebServlet(name = "DisasterUpdateServlet", urlPatterns = {
    "/updates", 
    "/admin/updates", 
    "/admin/updates/add", 
    "/admin/updates/delete"
})
public class DisasterUpdateServlet extends HttpServlet {
    private final DisasterUpdateService updateService = new DisasterUpdateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/updates".equals(path)) {
            List<DisasterUpdate> updates = updateService.getActiveUpdates();
            req.setAttribute("updates", updates);
            req.getRequestDispatcher("/WEB-INF/views/public/updates.jsp").forward(req, resp);
        } else if ("/admin/updates".equals(path)) {
            HttpSession session = req.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
            if (currentUser == null || !currentUser.hasAdministrativePrivileges()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            List<DisasterUpdate> updates = updateService.getAllUpdates();
            req.setAttribute("updates", updates);
            req.setAttribute("disasterTypes", DisasterType.values());
            req.getRequestDispatcher("/WEB-INF/views/admin/updates-manage.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null || !currentUser.hasAdministrativePrivileges()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if ("/admin/updates/add".equals(path)) {
            String title = req.getParameter("title");
            String content = req.getParameter("content");
            String disasterTypeStr = req.getParameter("disasterType");
            String severity = req.getParameter("severity");
            String affectedAreas = req.getParameter("affectedAreas");

            DisasterUpdate update = new DisasterUpdate();
            update.setTitle(title);
            update.setContent(content);
            update.setDisasterType(DisasterType.fromString(disasterTypeStr));
            update.setSeverity(severity);
            update.setAffectedAreas(affectedAreas);
            update.setIssuedBy(currentUser.getId());
            update.setActive(true);

            updateService.createUpdate(update);
            session.setAttribute("successMessage", "Official Disaster Advisory broadcasted successfully.");
            resp.sendRedirect(req.getContextPath() + "/admin/updates");
        } else if ("/admin/updates/delete".equals(path)) {
            Long updateId = Long.parseLong(req.getParameter("updateId"));
            updateService.deleteUpdate(updateId);
            session.setAttribute("successMessage", "Disaster update archived/removed.");
            resp.sendRedirect(req.getContextPath() + "/admin/updates");
        }
    }
}

