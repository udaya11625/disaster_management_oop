package com.disaster.web.servlet;

import com.disaster.model.DisasterReport;
import com.disaster.model.User;
import com.disaster.model.enums.DisasterType;
import com.disaster.model.enums.ReportStatus;
import com.disaster.model.enums.Severity;
import com.disaster.service.DisasterReportService;
import com.disaster.web.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * Controller for Disaster Reporting and Public Reports Directory.
 */
@WebServlet(name = "DisasterReportServlet", urlPatterns = {"/reports", "/report-disaster", "/admin/reports/update-status"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class DisasterReportServlet extends HttpServlet {
    private final DisasterReportService reportService = new DisasterReportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/reports".equals(path)) {
            String keyword = req.getParameter("keyword");
            String type = req.getParameter("type");
            String severity = req.getParameter("severity");
            String status = req.getParameter("status");

            List<DisasterReport> reports = reportService.searchReports(keyword, type, severity, status);
            req.setAttribute("reports", reports);
            req.setAttribute("pKeyword", keyword);
            req.setAttribute("pType", type);
            req.setAttribute("pSeverity", severity);
            req.setAttribute("pStatus", status);
            req.setAttribute("disasterTypes", DisasterType.values());
            req.setAttribute("severities", Severity.values());
            req.setAttribute("statuses", ReportStatus.values());

            req.getRequestDispatcher("/WEB-INF/views/public/reports.jsp").forward(req, resp);
        } else if ("/report-disaster".equals(path)) {
            req.setAttribute("disasterTypes", DisasterType.values());
            req.setAttribute("severities", Severity.values());
            req.getRequestDispatcher("/WEB-INF/views/public/report-disaster.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/report-disaster".equals(path)) {
            handleCreateReport(req, resp);
        } else if ("/admin/reports/update-status".equals(path)) {
            handleUpdateStatus(req, resp);
        }
    }

    private void handleCreateReport(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        String title = req.getParameter("title");
        String disasterTypeStr = req.getParameter("disasterType");
        String severityStr = req.getParameter("severity");
        String location = req.getParameter("location");
        String landmark = req.getParameter("landmark");
        String description = req.getParameter("description");
        String imageUrlParam = req.getParameter("imageUrl");

        // Validate required fields
        if (!ValidationUtils.isNotEmpty(title) || !ValidationUtils.isNotEmpty(location) || 
            !ValidationUtils.isNotEmpty(description)) {
            req.setAttribute("errorMessage", "Please fill in all mandatory fields (Title, Disaster Type, Location, Description).");
            req.setAttribute("disasterTypes", DisasterType.values());
            req.setAttribute("severities", Severity.values());
            req.getRequestDispatcher("/WEB-INF/views/public/report-disaster.jsp").forward(req, resp);
            return;
        }

        // Handle file upload or image URL
        String finalImageUrl = null;
        try {
            Part filePart = req.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String ext = "";
                int dotIndex = submittedFileName.lastIndexOf('.');
                if (dotIndex > 0) ext = submittedFileName.substring(dotIndex);

                String newFileName = "report_" + UUID.randomUUID().toString() + ext;
                String uploadPath = req.getServletContext().getRealPath("/uploads");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                filePart.write(uploadPath + File.separator + newFileName);
                finalImageUrl = req.getContextPath() + "/uploads/" + newFileName;
            }
        } catch (Exception ex) {
            // Upload failure fallback
        }

        if (finalImageUrl == null && ValidationUtils.isNotEmpty(imageUrlParam)) {
            finalImageUrl = imageUrlParam.trim();
        }

        DisasterReport report = new DisasterReport();
        report.setTitle(title.trim());
        report.setDisasterType(DisasterType.fromString(disasterTypeStr));
        report.setSeverity(Severity.fromString(severityStr));
        report.setLocation(location.trim());
        report.setLandmark(landmark != null ? landmark.trim() : "");
        report.setDescription(description.trim());
        report.setImageUrl(finalImageUrl);
        report.setStatus(ReportStatus.PENDING);
        if (currentUser != null) {
            report.setReporterId(currentUser.getId());
        }

        DisasterReport saved = reportService.submitReport(report);
        session.setAttribute("successMessage", "Disaster Report submitted successfully! Reference Tracking Code: " + saved.getCode());
        resp.sendRedirect(req.getContextPath() + "/reports");
    }

    private void handleUpdateStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null || !currentUser.hasAdministrativePrivileges()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long reportId = Long.parseLong(req.getParameter("reportId"));
        ReportStatus status = ReportStatus.fromString(req.getParameter("status"));
        String adminNotes = req.getParameter("adminNotes");

        reportService.updateReportStatus(reportId, status, currentUser.getId(), adminNotes);
        session.setAttribute("successMessage", "Report status updated to: " + status.getDisplayName());
        resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
    }
}

