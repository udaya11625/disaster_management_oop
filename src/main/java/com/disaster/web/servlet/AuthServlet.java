package com.disaster.web.servlet;

import com.disaster.model.User;
import com.disaster.model.enums.Role;
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
 * Authentication Controller handling Login, Registration, and Session Invalidation (Logout).
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/logout".equals(path)) {
            handleLogout(req, resp);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            User currentUser = (User) session.getAttribute("currentUser");
            resp.sendRedirect(req.getContextPath() + currentUser.getDashboardUrl());
            return;
        }

        if ("/login".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/public/login.jsp").forward(req, resp);
        } else if ("/register".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/public/register.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/login".equals(path)) {
            handleLogin(req, resp);
        } else if ("/register".equals(path)) {
            handleRegister(req, resp);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String redirect = req.getParameter("redirect");

        if (!ValidationUtils.isNotEmpty(username) || !ValidationUtils.isNotEmpty(password)) {
            req.setAttribute("errorMessage", "Please provide both username/email and password.");
            req.getRequestDispatcher("/WEB-INF/views/public/login.jsp").forward(req, resp);
            return;
        }

        Optional<User> userOpt = userService.authenticate(username, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", user);
            session.setAttribute("userRole", user.getRole().name());
            session.setAttribute("userName", user.getFullName());

            if (ValidationUtils.isNotEmpty(redirect) && !redirect.contains("login") && !redirect.contains("register")) {
                resp.sendRedirect(redirect);
            } else {
                resp.sendRedirect(req.getContextPath() + user.getDashboardUrl());
            }
        } else {
            req.setAttribute("errorMessage", "Invalid username or password. Please verify your credentials.");
            req.setAttribute("enteredUsername", username);
            req.getRequestDispatcher("/WEB-INF/views/public/login.jsp").forward(req, resp);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String roleStr = req.getParameter("role");

        // Server-side validation
        if (!ValidationUtils.isNotEmpty(username) || !ValidationUtils.isNotEmpty(email) ||
            !ValidationUtils.isNotEmpty(password) || !ValidationUtils.isNotEmpty(fullName) ||
            !ValidationUtils.isNotEmpty(phone)) {
            req.setAttribute("errorMessage", "Please complete all required fields.");
            preserveFormData(req);
            req.getRequestDispatcher("/WEB-INF/views/public/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Passwords do not match.");
            preserveFormData(req);
            req.getRequestDispatcher("/WEB-INF/views/public/register.jsp").forward(req, resp);
            return;
        }

        if (password.length() < 6) {
            req.setAttribute("errorMessage", "Password must be at least 6 characters long.");
            preserveFormData(req);
            req.getRequestDispatcher("/WEB-INF/views/public/register.jsp").forward(req, resp);
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            req.setAttribute("errorMessage", "Please enter a valid email address.");
            preserveFormData(req);
            req.getRequestDispatcher("/WEB-INF/views/public/register.jsp").forward(req, resp);
            return;
        }

        Role requestedRole = Role.fromString(roleStr);
        if (requestedRole == Role.ADMIN) {
            req.setAttribute("errorMessage", "Administrator accounts cannot be self-registered. Registration denied.");
            preserveFormData(req);
            req.getRequestDispatcher("/WEB-INF/views/public/register.jsp").forward(req, resp);
            return;
        }

        try {
            User newUser = userService.registerUser(username.trim(), email.trim(), password, fullName.trim(), phone.trim(), address != null ? address.trim() : "", requestedRole);
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", newUser);
            session.setAttribute("userRole", newUser.getRole().name());
            session.setAttribute("userName", newUser.getFullName());
            session.setAttribute("successMessage", "Account created successfully! Welcome to the Disaster Management Network.");
            resp.sendRedirect(req.getContextPath() + newUser.getDashboardUrl());
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            preserveFormData(req);
            req.getRequestDispatcher("/WEB-INF/views/public/register.jsp").forward(req, resp);
        }
    }

    private void handleLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/login?logout=true");
    }

    private void preserveFormData(HttpServletRequest req) {
        req.setAttribute("pUsername", req.getParameter("username"));
        req.setAttribute("pEmail", req.getParameter("email"));
        req.setAttribute("pFullName", req.getParameter("fullName"));
        req.setAttribute("pPhone", req.getParameter("phone"));
        req.setAttribute("pAddress", req.getParameter("address"));
        req.setAttribute("pRole", req.getParameter("role"));
    }
}

