<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Sign In - Disaster Emergency Management" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5 bg-light flex-grow-1">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-5 col-md-8">
                <!-- Login Card -->
                <div class="card card-theme shadow-lg border-0 rounded-4 overflow-hidden">
                    <div class="bg-primary-subtle p-4 text-center border-bottom border-light">
                        <div class="brand-icon mx-auto mb-2" style="width: 48px; height: 48px; font-size: 1.5rem;">
                            <i class="bi bi-shield-lock-fill"></i>
                        </div>
                        <h4 class="fw-bold text-dark mb-1">Disaster Portal Sign In</h4>
                        <p class="text-muted small mb-0">Access role-based operational dashboards and incident response tools.</p>
                    </div>

                    <div class="card-body p-4 p-md-5">
                        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

                        <c:if test="${param.logout == 'true'}">
                            <div class="alert alert-info alert-dismissible fade show small mb-4" role="alert">
                                <i class="bi bi-check-circle me-1"></i>You have been logged out successfully.
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/login" method="POST" class="needs-validation" novalidate>
                            <input type="hidden" name="redirect" value="<c:out value='${param.redirect}' />">

                            <div class="mb-3">
                                <label class="form-label">Username or Registered Email <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="bi bi-person"></i></span>
                                    <input type="text" id="usernameInput" name="username" class="form-control" placeholder="Enter username or email" 
                                           value="<c:out value='${enteredUsername}' />" required autofocus>
                                </div>
                                <div class="invalid-feedback">Username or email is required.</div>
                            </div>

                            <div class="mb-4">
                                <label class="form-label">Password <span class="text-danger">*</span></label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="bi bi-key"></i></span>
                                    <input type="password" id="passwordInput" name="password" class="form-control" placeholder="Enter your password" required>
                                </div>
                                <div class="invalid-feedback">Password is required.</div>
                            </div>

                            <button type="submit" class="btn btn-primary w-100 py-2 fw-bold fs-6 mb-3 shadow-sm">
                                <i class="bi bi-box-arrow-in-right me-2"></i>Sign In to Dashboard
                            </button>

                            <div class="text-center text-muted small">
                                Don't have an account? 
                                <a href="${pageContext.request.contextPath}/register" class="text-danger fw-bold text-decoration-none">Register as Citizen</a>
                                or 
                                <a href="${pageContext.request.contextPath}/volunteer-register" class="text-success fw-bold text-decoration-none">Volunteer</a>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- One-Click Demo Credentials Chip Box -->
                <div class="card border-0 bg-white shadow-sm rounded-4 mt-4 p-4">
                    <h6 class="fw-bold text-dark mb-2"><i class="bi bi-person-check-fill text-warning me-2"></i>Demo Evaluation Accounts (1-Click Fill)</h6>
                    <p class="text-muted small mb-3">Click any credential card below to automatically populate the sign-in form:</p>

                    <div class="row g-2">
                        <div class="col-6">
                            <button type="button" class="btn btn-outline-danger btn-sm w-100 text-start p-2" onclick="fillCredentials('admin', 'admin123')">
                                <div class="fw-bold"><i class="bi bi-shield-lock me-1"></i>Administrator</div>
                                <small class="text-muted font-monospace">admin / admin123</small>
                            </button>
                        </div>
                        <div class="col-6">
                            <button type="button" class="btn btn-outline-primary btn-sm w-100 text-start p-2" onclick="fillCredentials('captain_sarah', 'password123')">
                                <div class="fw-bold"><i class="bi bi-shield-shaded me-1"></i>Rescue Lead</div>
                                <small class="text-muted font-monospace">captain_sarah / pass</small>
                            </button>
                        </div>
                        <div class="col-6">
                            <button type="button" class="btn btn-outline-success btn-sm w-100 text-start p-2" onclick="fillCredentials('david_volunteer', 'password123')">
                                <div class="fw-bold"><i class="bi bi-heart me-1"></i>Volunteer</div>
                                <small class="text-muted font-monospace">david_volunteer / pass</small>
                            </button>
                        </div>
                        <div class="col-6">
                            <button type="button" class="btn btn-outline-info btn-sm w-100 text-start p-2" onclick="fillCredentials('citizen_john', 'password123')">
                                <div class="fw-bold text-dark"><i class="bi bi-person me-1"></i>Citizen</div>
                                <small class="text-muted font-monospace">citizen_john / pass</small>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
function fillCredentials(user, pass) {
    document.getElementById('usernameInput').value = user;
    document.getElementById('passwordInput').value = pass;
}
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
