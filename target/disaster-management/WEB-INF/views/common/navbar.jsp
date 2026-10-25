<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar navbar-expand-lg navbar-dark navbar-emergency sticky-top">
    <div class="container-fluid px-lg-4">
        <a class="navbar-brand navbar-brand-logo" href="${pageContext.request.contextPath}/home">
            <span class="brand-icon"><i class="bi bi-shield-fill-exclamation"></i></span>
            <span>DisasterRescue<span class="text-danger">.GOV</span></span>
        </a>

        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarMain">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home"><i class="bi bi-house-door me-1"></i>Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/reports"><i class="bi bi-broadcast me-1"></i>Disaster Reports</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/shelters"><i class="bi bi-building me-1"></i>Find Shelters</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/updates"><i class="bi bi-megaphone me-1"></i>Advisories</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/about"><i class="bi bi-info-circle me-1"></i>About Us</a>
                </li>

                <!-- Role Specific Shortcut Links -->
                <c:if test="${not empty sessionScope.currentUser}">
                    <c:choose>
                        <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
                            <li class="nav-item">
                                <a class="nav-link text-warning fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">
                                    <i class="bi bi-speedometer2 me-1"></i>Admin Console
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/admin/shelters">
                                    <i class="bi bi-door-open me-1"></i>Shelters
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/admin/resources">
                                    <i class="bi bi-boxes me-1"></i>Relief Supplies
                                </a>
                            </li>
                        </c:when>
                        <c:when test="${sessionScope.currentUser.role == 'RESCUE_TEAM'}">
                            <li class="nav-item">
                                <a class="nav-link text-info fw-bold" href="${pageContext.request.contextPath}/rescue/dashboard">
                                    <i class="bi bi-shield-shaded me-1"></i>Rescue Operations
                                </a>
                            </li>
                        </c:when>
                        <c:when test="${sessionScope.currentUser.role == 'VOLUNTEER'}">
                            <li class="nav-item">
                                <a class="nav-link text-success fw-bold" href="${pageContext.request.contextPath}/volunteer/dashboard">
                                    <i class="bi bi-heart me-1"></i>Volunteer Hub
                                </a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link text-info fw-bold" href="${pageContext.request.contextPath}/citizen/dashboard">
                                    <i class="bi bi-person-badge me-1"></i>My Dashboard
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </ul>

            <div class="d-flex align-items-center gap-2 flex-wrap">
                <!-- Action Buttons -->
                <a href="${pageContext.request.contextPath}/report-disaster" class="btn btn-sm btn-outline-light">
                    <i class="bi bi-plus-circle me-1"></i>Report Incident
                </a>
                <a href="${pageContext.request.contextPath}/request-assistance" class="btn btn-sm btn-emergency-nav">
                    <i class="bi bi-telephone-fill me-1"></i>Request SOS Assistance
                </a>

                <!-- User Dropdown or Login -->
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <div class="dropdown ms-2">
                            <button class="btn btn-sm btn-outline-light dropdown-toggle d-flex align-items-center gap-1" type="button" data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle"></i>
                                <span><c:out value="${sessionScope.currentUser.fullName}" /></span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end shadow">
                                <li class="px-3 py-2 text-muted border-bottom">
                                    <small>Signed in as:</small>
                                    <div class="fw-bold text-dark"><c:out value="${sessionScope.currentUser.username}" /></div>
                                    <span class="badge bg-primary mt-1"><c:out value="${sessionScope.currentUser.role.displayName}" /></span>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}${sessionScope.currentUser.dashboardUrl}">
                                        <i class="bi bi-speedometer2 me-2"></i>My Dashboard
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        <i class="bi bi-gear me-2"></i>Profile & Account
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="bi bi-box-arrow-right me-2"></i>Sign Out
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="d-flex gap-2 ms-2">
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-sm btn-outline-light">
                                <i class="bi bi-box-arrow-in-right me-1"></i>Login
                            </a>
                            <a href="${pageContext.request.contextPath}/register" class="btn btn-sm btn-warning text-dark fw-bold">
                                <i class="bi bi-person-plus me-1"></i>Register
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>
