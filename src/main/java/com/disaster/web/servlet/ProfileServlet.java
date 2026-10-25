package com.disaster.web.servlet;

import com.disaster.model.User;
import com.disaster.service.UserService;
import com.disaster.web.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller for User Profile and Account Settings.
 */
@WebServlet(name = "ProfileServlet", urlPatterns = "/profile")
public class ProfileServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Refresh user from database
        Optional<User> freshUser = userService.getUserById(currentUser.getId());
        freshUser.ifPresent(u -> {
            session.setAttribute("currentUser", u);
            req.setAttribute("user", u);
        });

        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if ("updateProfile".equals(action)) {
            String fullName = req.getParameter("fullName");
            String phone = req.getParameter("phone");
            String address = req.getParameter("address");

            if (ValidationUtils.isNotEmpty(fullName) && ValidationUtils.isNotEmpty(phone)) {
                currentUser.setFullName(fullName.trim());
                currentUser.setPhone(phone.trim());
                currentUser.setAddress(address != null ? address.trim() : "");
                userService.updateProfile(currentUser);
                session.setAttribute("currentUser", currentUser);
                session.setAttribute("userName", currentUser.getFullName());
                req.setAttribute("successMessage", "Profile details updated successfully.");
            } else {
                req.setAttribute("errorMessage", "Full Name and Phone cannot be blank.");
            }
        } else if ("changePassword".equals(action)) {
            String oldPass = req.getParameter("currentPassword");
            String newPass = req.getParameter("newPassword");
            String confirmPass = req.getParameter("confirmPassword");

            if (!ValidationUtils.isNotEmpty(oldPass) || !ValidationUtils.isNotEmpty(newPass)) {
                req.setAttribute("errorMessage", "Please provide your current password and new password.");
            } else if (!newPass.equals(confirmPass)) {
                req.setAttribute("errorMessage", "New password and confirmation do not match.");
            } else if (newPass.length() < 6) {
                req.setAttribute("errorMessage", "New password must be at least 6 characters.");
            } else {
                boolean changed = userService.changePassword(currentUser.getId(), oldPass, newPass);
                if (changed) {
                    req.setAttribute("successMessage", "Password updated successfully.");
                } else {
                    req.setAttribute("errorMessage", "Current password verification failed. Please try again.");
                }
            }
        }

        req.setAttribute("user", currentUser);
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }
}

