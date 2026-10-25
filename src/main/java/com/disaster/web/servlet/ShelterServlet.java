package com.disaster.web.servlet;

import com.disaster.model.Shelter;
import com.disaster.model.ShelterAllocation;
import com.disaster.model.User;
import com.disaster.model.enums.ShelterStatus;
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
 * Controller for Shelter Management, Search, and Evacuee Check-In/Check-Out.
 */
@WebServlet(name = "ShelterServlet", urlPatterns = {
    "/shelters", 
    "/admin/shelters", 
    "/admin/shelters/add", 
    "/admin/shelters/update", 
    "/admin/shelters/check-in", 
    "/admin/shelters/check-out"
})
public class ShelterServlet extends HttpServlet {
    private final ShelterService shelterService = new ShelterService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/shelters".equals(path)) {
            String keyword = req.getParameter("keyword");
            String city = req.getParameter("city");
            boolean medicalOnly = "true".equalsIgnoreCase(req.getParameter("medicalOnly"));

            List<Shelter> shelters = shelterService.searchShelters(keyword, city, medicalOnly);
            req.setAttribute("shelters", shelters);
            req.setAttribute("pKeyword", keyword);
            req.setAttribute("pCity", city);
            req.setAttribute("pMedicalOnly", medicalOnly);

            req.getRequestDispatcher("/WEB-INF/views/public/shelters.jsp").forward(req, resp);
        } else if ("/admin/shelters".equals(path)) {
            List<Shelter> allShelters = shelterService.getAllShelters();
            req.setAttribute("shelters", allShelters);
            req.getRequestDispatcher("/WEB-INF/views/admin/shelters-manage.jsp").forward(req, resp);
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

        if ("/admin/shelters/add".equals(path)) {
            handleAddShelter(req, resp);
        } else if ("/admin/shelters/update".equals(path)) {
            handleUpdateShelter(req, resp);
        } else if ("/admin/shelters/check-in".equals(path)) {
            handleCheckIn(req, resp);
        } else if ("/admin/shelters/check-out".equals(path)) {
            handleCheckOut(req, resp);
        }
    }

    private void handleAddShelter(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String city = req.getParameter("city");
        String contactPerson = req.getParameter("contactPerson");
        String contactPhone = req.getParameter("contactPhone");
        int totalCapacity = ValidationUtils.parsePositiveInt(req.getParameter("totalCapacity"), 100);

        Shelter shelter = new Shelter();
        shelter.setName(name);
        shelter.setAddress(address);
        shelter.setCity(city);
        shelter.setContactPerson(contactPerson);
        shelter.setContactPhone(contactPhone);
        shelter.setTotalCapacity(totalCapacity);
        shelter.setHasFood(req.getParameter("hasFood") != null);
        shelter.setHasWater(req.getParameter("hasWater") != null);
        shelter.setHasSanitation(req.getParameter("hasSanitation") != null);
        shelter.setHasMedical(req.getParameter("hasMedical") != null);
        shelter.setHasElectricity(req.getParameter("hasElectricity") != null);
        shelter.setHasBedding(req.getParameter("hasBedding") != null);
        shelter.setStatus(ShelterStatus.ACTIVE);

        shelterService.addShelter(shelter);
        req.getSession().setAttribute("successMessage", "New emergency shelter added successfully.");
        resp.sendRedirect(req.getContextPath() + "/admin/shelters");
    }

    private void handleUpdateShelter(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String city = req.getParameter("city");
        String contactPerson = req.getParameter("contactPerson");
        String contactPhone = req.getParameter("contactPhone");
        int totalCapacity = ValidationUtils.parsePositiveInt(req.getParameter("totalCapacity"), 100);
        String statusStr = req.getParameter("status");

        shelterService.getShelterById(id).ifPresent(s -> {
            s.setName(name);
            s.setAddress(address);
            s.setCity(city);
            s.setContactPerson(contactPerson);
            s.setContactPhone(contactPhone);
            s.setTotalCapacity(totalCapacity);
            s.setStatus(ShelterStatus.fromString(statusStr));
            s.setHasFood(req.getParameter("hasFood") != null);
            s.setHasWater(req.getParameter("hasWater") != null);
            s.setHasSanitation(req.getParameter("hasSanitation") != null);
            s.setHasMedical(req.getParameter("hasMedical") != null);
            s.setHasElectricity(req.getParameter("hasElectricity") != null);
            s.setHasBedding(req.getParameter("hasBedding") != null);
            shelterService.updateShelter(s);
        });

        req.getSession().setAttribute("successMessage", "Shelter information updated successfully.");
        resp.sendRedirect(req.getContextPath() + "/admin/shelters");
    }

    private void handleCheckIn(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long shelterId = Long.parseLong(req.getParameter("shelterId"));
        String repName = req.getParameter("repName");
        String phone = req.getParameter("phone");
        int peopleCount = ValidationUtils.parsePositiveInt(req.getParameter("peopleCount"), 1);
        String notes = req.getParameter("notes");

        boolean success = shelterService.checkInEvacuees(shelterId, repName, phone, peopleCount, notes);
        if (success) {
            req.getSession().setAttribute("successMessage", "Check-in successful! Evacuees allocated to shelter.");
        } else {
            req.getSession().setAttribute("errorMessage", "Check-in failed! Shelter does not have sufficient available capacity or is inactive.");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/shelters");
    }

    private void handleCheckOut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long allocationId = Long.parseLong(req.getParameter("allocationId"));
        boolean success = shelterService.checkOutEvacuees(allocationId);
        if (success) {
            req.getSession().setAttribute("successMessage", "Evacuees checked out. Shelter capacity released.");
        } else {
            req.getSession().setAttribute("errorMessage", "Check-out operation could not be completed.");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/shelters");
    }
}

