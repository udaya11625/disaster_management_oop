<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Profile & Account Settings" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5 bg-light flex-grow-1">
    <div class="container">
        <!-- Header -->
        <div class="mb-4">
            <h2 class="fw-bold text-dark mb-1"><i class="bi bi-person-gear text-primary me-2"></i>Profile & Security Settings</h2>
            <p class="text-muted small mb-0">Manage your contact details, emergency callbacks, and account credentials.</p>
        </div>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <div class="row g-4">
            <!-- Left: Profile Information & Edit Form -->
            <div class="col-lg-7">
                <div class="card card-theme shadow-sm">
                    <div class="card-header bg-white d-flex justify-content-between align-items-center">
                        <span class="fw-bold"><i class="bi bi-person-lines-fill me-2 text-primary"></i>Personal Information</span>
                        ${user.roleBadge}
                    </div>
                    <div class="card-body p-4">
                        <form action="${pageContext.request.contextPath}/profile" method="POST">
                            <input type="hidden" name="action" value="updateProfile">

                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">Username</label>
                                    <input type="text" class="form-control bg-light" value="<c:out value='${user.username}' />" readonly>
                                    <div class="form-text small">System identification code (fixed).</div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Email Address</label>
                                    <input type="email" class="form-control bg-light" value="<c:out value='${user.email}' />" readonly>
                                    <div class="form-text small">Primary account email.</div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Full Name <span class="text-danger">*</span></label>
                                <input type="text" name="fullName" class="form-control" value="<c:out value='${user.fullName}' />" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Primary Mobile / Callback Phone <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="bi bi-telephone"></i></span>
                                    <input type="tel" name="phone" class="form-control" value="<c:out value='${user.phone}' />" required>
                                </div>
                            </div>

                            <div class="mb-4">
                                <label class="form-label">Residential Address / Sector</label>
                                <input type="text" name="address" class="form-control" value="<c:out value='${user.address}' />">
                            </div>

                            <button type="submit" class="btn btn-primary fw-bold px-4">
                                <i class="bi bi-check-lg me-1"></i>Save Profile Changes
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Right: Change Password Card -->
            <div class="col-lg-5">
                <div class="card card-theme shadow-sm mb-4">
                    <div class="card-header bg-white">
                        <span class="fw-bold"><i class="bi bi-key-fill text-danger me-2"></i>Change Account Password</span>
                    </div>
                    <div class="card-body p-4">
                        <form action="${pageContext.request.contextPath}/profile" method="POST">
                            <input type="hidden" name="action" value="changePassword">

                            <div class="mb-3">
                                <label class="form-label">Current Password <span class="text-danger">*</span></label>
                                <input type="password" name="currentPassword" class="form-control" placeholder="Enter current password" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">New Password <span class="text-danger">*</span></label>
                                <input type="password" name="newPassword" class="form-control" placeholder="At least 6 characters" minlength="6" required>
                            </div>

                            <div class="mb-4">
                                <label class="form-label">Confirm New Password <span class="text-danger">*</span></label>
                                <input type="password" name="confirmPassword" class="form-control" placeholder="Re-enter new password" minlength="6" required>
                            </div>

                            <button type="submit" class="btn btn-danger fw-bold w-100">
                                <i class="bi bi-shield-lock me-1"></i>Update Password
                            </button>
                        </form>
                    </div>
                </div>

                <!-- Account Status Summary Card -->
                <div class="card bg-white border rounded-3 p-3">
                    <div class="small text-muted mb-1">Account Role & Privileges:</div>
                    <div class="fw-bold text-dark fs-6 mb-2">${user.role.displayName}</div>
                    <div class="small text-muted mb-1">Account Status:</div>
                    <span class="badge bg-success w-auto align-self-start mb-2"><i class="bi bi-check-circle me-1"></i>Active / Verified</span>
                    <div class="small text-muted">Member Since: <c:out value="${user.createdAt}" /></div>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

