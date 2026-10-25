package com.disaster.web.servlet;

import com.disaster.dao.DashboardDAO;
import com.disaster.model.DisasterReport;
import com.disaster.model.DisasterUpdate;
import com.disaster.model.Shelter;
import com.disaster.service.DisasterReportService;
import com.disaster.service.DisasterUpdateService;
import com.disaster.service.ShelterService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Homepage Controller.
 * Presents system overview, real-time metrics, recent advisories, and emergency shortcuts.
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {
    private final DisasterReportService reportService = new DisasterReportService();
    private final DisasterUpdateService updateService = new DisasterUpdateService();
    private final ShelterService shelterService = new ShelterService();
    private final DashboardDAO dashboardDAO = new DashboardDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<DisasterUpdate> activeUpdates = updateService.getActiveUpdates();
        List<DisasterReport> recentReports = reportService.getAllReports();
        if (recentReports.size() > 4) {
            recentReports = recentReports.subList(0, 4);
        }
        List<Shelter> activeShelters = shelterService.getActiveShelters();
        if (activeShelters.size() > 4) {
            activeShelters = activeShelters.subList(0, 4);
        }
        Map<String, Object> stats = dashboardDAO.getAdminDashboardStats();

        req.setAttribute("activeUpdates", activeUpdates);
        req.setAttribute("recentReports", recentReports);
        req.setAttribute("activeShelters", activeShelters);
        req.setAttribute("stats", stats);

        req.getRequestDispatcher("/WEB-INF/views/public/index.jsp").forward(req, resp);
    }
}

