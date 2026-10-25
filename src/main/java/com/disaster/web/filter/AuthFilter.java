package com.disaster.web.filter;

import com.disaster.model.User;
import com.disaster.model.enums.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Robust Authentication & Role-Based Authorization Filter.
 * Enforces session security and URL route protection.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getServletPath();
        if (path == null) path = "";

        // Allow static assets, public resources, and public pages
        if (isPublicResource(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        // If not logged in and accessing protected endpoint
        if (currentUser == null) {
            if (isProtectedPath(path)) {
                String target = req.getRequestURI();
                if (req.getQueryString() != null) {
                    target += "?" + req.getQueryString();
                }
                req.getSession(true).setAttribute("errorMessage", "Please log in to access this feature.");
                resp.sendRedirect(req.getContextPath() + "/login?redirect=" + java.net.URLEncoder.encode(target, "UTF-8"));
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        // Role-based Access Control (RBAC) Enforcement
        Role userRole = currentUser.getRole();

        // 1. Admin paths: strictly ADMIN
        if (path.startsWith("/admin")) {
            if (userRole != Role.ADMIN) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Administrative privileges required.");
                return;
            }
        }

        // 2. Rescue Team paths: RESCUE_TEAM or ADMIN
        else if (path.startsWith("/rescue")) {
            if (userRole != Role.RESCUE_TEAM && userRole != Role.ADMIN) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Rescue Team personnel access only.");
                return;
            }
        }

        // 3. Volunteer paths: VOLUNTEER or ADMIN
        else if (path.startsWith("/volunteer")) {
            if (userRole != Role.VOLUNTEER && userRole != Role.ADMIN) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Registered Volunteer access only.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicResource(String path) {
        return path.startsWith("/assets/") ||
               path.startsWith("/uploads/") ||
               path.startsWith("/api/") ||
               path.equals("/login") ||
               path.equals("/register") ||
               path.equals("/volunteer-register") ||
               path.equals("/about") ||
               path.equals("/shelters") ||
               path.equals("/reports") ||
               path.equals("/updates") ||
               path.equals("/index.jsp") ||
               path.equals("/") ||
               path.isEmpty();
    }

    private boolean isProtectedPath(String path) {
        return path.startsWith("/admin") ||
               path.startsWith("/rescue") ||
               path.startsWith("/volunteer") ||
               path.startsWith("/citizen") ||
               path.equals("/profile") ||
               path.equals("/report-disaster") ||
               path.equals("/request-assistance");
    }
}

