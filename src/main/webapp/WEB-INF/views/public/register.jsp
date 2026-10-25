<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Register Account - Disaster Emergency Management" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5 bg-light flex-grow-1">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-6 col-md-8">
                <!-- Registration Card -->
                <div class="card card-theme shadow-lg border-0 rounded-4 overflow-hidden">
                    <div class="bg-primary text-white p-4 text-center">
                        <h4 class="fw-bold mb-1">Create an Account</h4>
                        <p class="text-white-50 small mb-0">Join the centralized emergency management network to access assistance and coordinate disaster response.</p>
                    </div>

                    <div class="card-body p-4 p-md-5">
                        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

                        <form action="${pageContext.request.contextPath}/register" method="POST" class="needs-validation" novalidate>
                            
                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">Full Name <span class="text-danger">*</span></label>
                                    <input type="text" name="fullName" class="form-control" placeholder="John Doe" value="<c:out value='${pFullName}' />" required>
                                    <div class="invalid-feedback">Full name is required.</div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Username <span class="text-danger">*</span></label>
                                    <input type="text" name="username" class="form-control" placeholder="john_doe" value="<c:out value='${pUsername}' />" required>
                                    <div class="invalid-feedback">Choose a unique username.</div>
                                </div>
                            </div>

                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">Email Address <span class="text-danger">*</span></label>
                                    <input type="email" name="email" class="form-control" placeholder="john@example.com" value="<c:out value='${pEmail}' />" required>
                                    <div class="invalid-feedback">Valid email address required.</div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Mobile Phone Number <span class="text-danger">*</span></label>
                                    <input type="tel" name="phone" class="form-control" placeholder="+1-555-0123" value="<c:out value='${pPhone}' />" required>
                                    <div class="invalid-feedback">Contact number is required.</div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Residential Address / City</label>
                                <input type="text" name="address" class="form-control" placeholder="123 River Street, District 4" value="<c:out value='${pAddress}' />">
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Registration Role <span class="text-danger">*</span></label>
                                <select name="role" class="form-select" required>
                                    <option value="CITIZEN" ${pRole == 'CITIZEN' ? 'selected' : ''}>Citizen (General Public, Report & SOS Access)</option>
                                    <option value="VOLUNTEER" ${pRole == 'VOLUNTEER' ? 'selected' : ''}>Volunteer (Field Relief Support & Logistics)</option>
                                    <option value="RESCUE_TEAM" ${pRole == 'RESCUE_TEAM' ? 'selected' : ''}>Rescue Team Member (Emergency Response Crew)</option>
                                </select>
                                <div class="form-text text-muted small">
                                    <i class="bi bi-shield-lock me-1"></i>Administrator accounts must be provisioned securely by existing authority.
                                </div>
                            </div>

                            <div class="row g-3 mb-4">
                                <div class="col-md-6">
                                    <label class="form-label">Password <span class="text-danger">*</span></label>
                                    <input type="password" name="password" class="form-control" placeholder="Min 6 characters" minlength="6" required>
                                    <div class="invalid-feedback">At least 6 characters required.</div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Confirm Password <span class="text-danger">*</span></label>
                                    <input type="password" name="confirmPassword" class="form-control" placeholder="Re-enter password" minlength="6" required>
                                    <div class="invalid-feedback">Please confirm your password.</div>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-primary w-100 py-2 fw-bold fs-6 mb-3 shadow-sm">
                                <i class="bi bi-person-check-fill me-2"></i>Create My Account
                            </button>

                            <div class="text-center text-muted small">
                                Already registered? 
                                <a href="${pageContext.request.contextPath}/login" class="text-primary fw-bold text-decoration-none">Sign In Here</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

