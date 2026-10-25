package com.disaster.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Data Access Object for High-Performance Dashboard Analytics & Chart Visualizations.
 * Executes optimized aggregation queries directly against MySQL.
 */
public class DashboardDAO extends AbstractDAO<Object> {

    @Override
    protected Object mapResultSetToEntity(ResultSet rs) {
        return null;
    }

    public Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        try (Connection conn = getConnection()) {
            // 1. Disaster Reports Counts
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total, " +
                        "SUM(CASE WHEN status NOT IN ('RESOLVED', 'DISMISSED') THEN 1 ELSE 0 END) AS active_incidents " +
                        "FROM disaster_reports")) {
                    if (rs.next()) {
                        stats.put("totalReports", rs.getInt("total"));
                        stats.put("activeIncidents", rs.getInt("active_incidents"));
                    }
                }

                // 2. Assistance Requests Counts
                try (ResultSet rs = stmt.executeQuery("SELECT " +
                        "SUM(CASE WHEN priority = 'CRITICAL' AND status != 'RESOLVED' THEN 1 ELSE 0 END) AS critical_requests, " +
                        "SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END) AS pending_requests, " +
                        "COUNT(*) AS total_requests FROM assistance_requests")) {
                    if (rs.next()) {
                        stats.put("criticalRequests", rs.getInt("critical_requests"));
                        stats.put("pendingRequests", rs.getInt("pending_requests"));
                        stats.put("totalRequests", rs.getInt("total_requests"));
                    }
                }

                // 3. Shelters & Occupancy
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total_shelters, " +
                        "COALESCE(SUM(total_capacity), 0) AS total_capacity, " +
                        "COALESCE(SUM(current_occupancy), 0) AS total_occupancy, " +
                        "SUM(CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END) AS active_shelters " +
                        "FROM shelters")) {
                    if (rs.next()) {
                        int cap = rs.getInt("total_capacity");
                        int occ = rs.getInt("total_occupancy");
                        stats.put("totalShelters", rs.getInt("total_shelters"));
                        stats.put("activeShelters", rs.getInt("active_shelters"));
                        stats.put("totalCapacity", cap);
                        stats.put("totalOccupancy", occ);
                        stats.put("availableCapacity", Math.max(0, cap - occ));
                        double occRate = cap > 0 ? ((double) occ / cap) * 100.0 : 0.0;
                        stats.put("shelterOccupancyRate", Math.round(occRate * 10.0) / 10.0);
                    }
                }

                // 4. Volunteers & Rescue Teams
                try (ResultSet rs = stmt.executeQuery("SELECT " +
                        "(SELECT COUNT(*) FROM users WHERE role = 'VOLUNTEER') AS total_volunteers, " +
                        "(SELECT COUNT(*) FROM volunteer_profiles WHERE availability = 'AVAILABLE') AS available_volunteers, " +
                        "(SELECT COUNT(*) FROM rescue_teams) AS total_rescue_teams, " +
                        "(SELECT COUNT(*) FROM rescue_teams WHERE availability = 'AVAILABLE') AS available_teams")) {
                    if (rs.next()) {
                        stats.put("totalVolunteers", rs.getInt("total_volunteers"));
                        stats.put("availableVolunteers", rs.getInt("available_volunteers"));
                        stats.put("totalRescueTeams", rs.getInt("total_rescue_teams"));
                        stats.put("availableRescueTeams", rs.getInt("available_teams"));
                    }
                }

                // 5. Relief Inventory
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total_resources, " +
                        "COALESCE(SUM(quantity), 0) AS total_units, " +
                        "SUM(CASE WHEN quantity <= min_stock_level THEN 1 ELSE 0 END) AS low_stock_count " +
                        "FROM relief_resources")) {
                    if (rs.next()) {
                        stats.put("totalResources", rs.getInt("total_resources"));
                        stats.put("totalResourceUnits", rs.getInt("total_units"));
                        stats.put("lowStockCount", rs.getInt("low_stock_count"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error gathering admin dashboard stats: {}", e.getMessage(), e);
        }

        return stats;
    }

    public Map<String, Integer> getReportsByType() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT disaster_type, COUNT(*) as cnt FROM disaster_reports GROUP BY disaster_type ORDER BY cnt DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("disaster_type"), rs.getInt("cnt"));
            }
        } catch (SQLException e) {
            logger.error("Error fetching reports by type: {}", e.getMessage(), e);
        }
        return map;
    }

    public Map<String, Integer> getRequestsByStatus() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("PENDING", 0);
        map.put("ASSIGNED", 0);
        map.put("IN_PROGRESS", 0);
        map.put("RESOLVED", 0);

        String sql = "SELECT status, COUNT(*) as cnt FROM assistance_requests GROUP BY status";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("status"), rs.getInt("cnt"));
            }
        } catch (SQLException e) {
            logger.error("Error fetching requests by status: {}", e.getMessage(), e);
        }
        return map;
    }

    public List<Map<String, Object>> getShelterOccupancyComparison() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT name, total_capacity, current_occupancy FROM shelters ORDER BY total_capacity DESC LIMIT 8";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", rs.getString("name"));
                item.put("totalCapacity", rs.getInt("total_capacity"));
                item.put("currentOccupancy", rs.getInt("current_occupancy"));
                list.add(item);
            }
        } catch (SQLException e) {
            logger.error("Error fetching shelter occupancy comparison: {}", e.getMessage(), e);
        }
        return list;
    }

    public List<Map<String, Object>> getRecentActivity() {
        List<Map<String, Object>> activities = new ArrayList<>();
        // Union of recent reports, requests, distributions
        String sql = 
            "(SELECT 'REPORT' as act_type, report_code as act_code, title as act_title, " +
            " severity as act_severity, created_at as act_time FROM disaster_reports ORDER BY created_at DESC LIMIT 5) " +
            "UNION ALL " +
            "(SELECT 'REQUEST' as act_type, request_code as act_code, CONCAT(assistance_type, ' needed at ', location) as act_title, " +
            " priority as act_severity, created_at as act_time FROM assistance_requests ORDER BY created_at DESC LIMIT 5) " +
            "ORDER BY act_time DESC LIMIT 8";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> act = new HashMap<>();
                act.put("type", rs.getString("act_type"));
                act.put("code", rs.getString("act_code"));
                act.put("title", rs.getString("act_title"));
                act.put("severity", rs.getString("act_severity"));
                act.put("timestamp", rs.getTimestamp("act_time"));
                activities.add(act);
            }
        } catch (SQLException e) {
            logger.error("Error fetching recent activity: {}", e.getMessage(), e);
        }
        return activities;
    }
}

