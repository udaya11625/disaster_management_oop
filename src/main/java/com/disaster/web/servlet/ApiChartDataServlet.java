package com.disaster.web.servlet;

import com.disaster.dao.DashboardDAO;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * REST API Controller delivering database aggregation analytics for Chart.js visualizations.
 */
@WebServlet(name = "ApiChartDataServlet", urlPatterns = "/api/charts/data")
public class ApiChartDataServlet extends HttpServlet {
    private final DashboardDAO dashboardDAO = new DashboardDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> data = new HashMap<>();
        data.put("reportsByType", dashboardDAO.getReportsByType());
        data.put("requestsByStatus", dashboardDAO.getRequestsByStatus());
        data.put("shelterOccupancy", dashboardDAO.getShelterOccupancyComparison());

        resp.getWriter().write(gson.toJson(data));
    }
}

