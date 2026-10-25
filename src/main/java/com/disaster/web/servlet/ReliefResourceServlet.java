package com.disaster.web.servlet;

import com.disaster.model.ReliefResource;
import com.disaster.model.ResourceDistribution;
import com.disaster.model.Shelter;
import com.disaster.model.User;
import com.disaster.model.enums.ResourceCategory;
import com.disaster.service.ReliefResourceService;
import com.disaster.service.ShelterService;
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
 * Controller for Relief Inventory and Distribution Logistics.
 */
@WebServlet(name = "ReliefResourceServlet", urlPatterns = {
    "/admin/resources", 
    "/admin/resources/add", 
    "/admin/resources/restock", 
    "/admin/resources/distribute"
})
public class ReliefResourceServlet extends HttpServlet {
    private final ReliefResourceService resourceService = new ReliefResourceService();
    private final ShelterService shelterService = new ShelterService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null || !currentUser.hasAdministrativePrivileges()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String keyword = req.getParameter("keyword");
        String category = req.getParameter("category");

        List<ReliefResource> resources = resourceService.searchResources(keyword, category);
        List<ReliefResource> lowStock = resourceService.getLowStockResources();
        List<ResourceDistribution> distributions = resourceService.getAllDistributions();
        List<Shelter> shelters = shelterService.getActiveShelters();

        req.setAttribute("resources", resources);
        req.setAttribute("lowStock", lowStock);
        req.setAttribute("distributions", distributions);
        req.setAttribute("shelters", shelters);
        req.setAttribute("categories", ResourceCategory.values());
        req.setAttribute("pKeyword", keyword);
        req.setAttribute("pCategory", category);

        req.getRequestDispatcher("/WEB-INF/views/admin/resources-manage.jsp").forward(req, resp);
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

        if ("/admin/resources/add".equals(path)) {
            handleAddResource(req, resp);
        } else if ("/admin/resources/restock".equals(path)) {
            handleRestock(req, resp);
        } else if ("/admin/resources/distribute".equals(path)) {
            handleDistribute(req, resp, currentUser);
        }
    }

    private void handleAddResource(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String categoryStr = req.getParameter("category");
        int quantity = ValidationUtils.parsePositiveInt(req.getParameter("quantity"), 0);
        String unit = req.getParameter("unit");
        String storageLocation = req.getParameter("storageLocation");
        int minStockLevel = ValidationUtils.parsePositiveInt(req.getParameter("minStockLevel"), 50);

        ReliefResource resource = new ReliefResource();
        resource.setName(name);
        resource.setCategory(ResourceCategory.fromString(categoryStr));
        resource.setQuantity(quantity);
        resource.setUnit(unit);
        resource.setStorageLocation(storageLocation);
        resource.setMinStockLevel(minStockLevel);

        resourceService.addResource(resource);
        req.getSession().setAttribute("successMessage", "New relief resource added to inventory.");
        resp.sendRedirect(req.getContextPath() + "/admin/resources");
    }

    private void handleRestock(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long resourceId = Long.parseLong(req.getParameter("resourceId"));
        int addAmount = ValidationUtils.parsePositiveInt(req.getParameter("addAmount"), 0);

        if (addAmount > 0) {
            resourceService.replenishStock(resourceId, addAmount);
            req.getSession().setAttribute("successMessage", "Stock successfully replenished.");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/resources");
    }

    private void handleDistribute(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException {
        Long resourceId = Long.parseLong(req.getParameter("resourceId"));
        int quantity = ValidationUtils.parsePositiveInt(req.getParameter("quantity"), 1);
        String destType = req.getParameter("destinationType");
        String destName = req.getParameter("destinationName");
        String shelterIdStr = req.getParameter("shelterId");
        String recipientContact = req.getParameter("recipientContact");
        String remarks = req.getParameter("remarks");

        Long shelterId = ValidationUtils.isNotEmpty(shelterIdStr) ? Long.parseLong(shelterIdStr) : null;

        boolean success = resourceService.distributeResource(
            resourceId, quantity, destType, destName, shelterId, null, currentUser.getId(), recipientContact, remarks
        );

        if (success) {
            req.getSession().setAttribute("successMessage", "Relief items successfully dispatched and logged in distribution audit trail.");
        } else {
            req.getSession().setAttribute("errorMessage", "Distribution failed: Insufficient warehouse stock available.");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/resources");
    }
}

