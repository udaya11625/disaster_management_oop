<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Citizen Dashboard - Emergency Operations" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <!-- Welcome Banner -->
        <div class="p-4 rounded-4 bg-primary text-white shadow-sm mb-4 d-flex justify-content-between align-items-center flex-wrap gap-3">
            <div>
                <span class="badge bg-info text-dark mb-2"><i class="bi bi-person-badge me-1"></i>Citizen Portal</span>
                <h2 class="fw-bold mb-1">Welcome, <c:out value="${sessionScope.currentUser.fullName}" />!</h2>
                <p class="mb-0 text-white-50 small">Track your submitted disaster reports, monitor emergency SOS assistance requests, and view safe shelters.</p>
            </div>
            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/request-assistance" class="btn btn-danger fw-bold shadow-sm">
                    <i class="bi bi-telephone-fill me-1"></i>New SOS Request
                </a>
                <a href="${pageContext.request.contextPath}/report-disaster" class="btn btn-outline-light fw-semibold">
                    <i class="bi bi-plus-circle me-1"></i>Report Incident
                </a>
            </div>
        </div>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <!-- Citizen Stats Row -->
        <div class="row g-4 mb-4">
            <div class="col-md-4">
                <div class="stat-card">
                    <div class="stat-icon bg-primary bg-opacity-10 text-primary">
                        <i class="bi bi-broadcast"></i>
                    </div>
                    <div class="stat-value">${myReports.size()}</div>
                    <div class="stat-label">Reports Submitted by You</div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stat-card">
                    <div class="stat-icon bg-danger bg-opacity-10 text-danger">
                        <i class="bi bi-life-preserver"></i>
                    </div>
                    <div class="stat-value">${myRequests.size()}</div>
                    <div class="stat-label">Emergency SOS Requests</div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stat-card">
                    <div class="stat-icon bg-success bg-opacity-10 text-success">
                        <i class="bi bi-shield-check"></i>
                    </div>
                    <div class="stat-value">${nearbyShelters.size()}</div>
                    <div class="stat-label">Nearby Active Shelters</div>
                </div>
            </div>
        </div>

        <!-- 1. My Emergency Assistance Requests -->
        <div class="card card-theme mb-4">
            <div class="card-header bg-danger text-white">
                <span class="fw-bold"><i class="bi bi-heart-pulse-fill me-2"></i>My Emergency SOS Assistance Requests</span>
                <a href="${pageContext.request.contextPath}/request-assistance" class="btn btn-sm btn-light text-danger fw-bold">Request Aid</a>
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${not empty myRequests}">
                        <div class="table-responsive">
                            <table class="table table-custom table-hover">
                                <thead>
                                    <tr>
                                        <th>Request Code</th>
                                        <th>Assistance Type</th>
                                        <th>Location</th>
                                        <th>People Affected</th>
                                        <th>Priority</th>
                                        <th>Assigned Rescue Team</th>
                                        <th>Current Status</th>
                                        <th>Resolution Notes</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="req" items="${myRequests}">
                                        <tr>
                                            <td><span class="font-monospace fw-bold text-dark"><c:out value="${req.code}" /></span></td>
                                            <td><span class="badge bg-secondary"><c:out value="${req.assistanceType}" /></span></td>
                                            <td>
                                                <i class="bi bi-geo-alt-fill text-danger me-1"></i><c:out value="${req.location}" />
                                                <c:if test="${not empty req.landmark}">
                                                    <div class="small text-muted"><c:out value="${req.landmark}" /></div>
                                                </c:if>
                                            </td>
                                            <td><strong><c:out value="${req.peopleAffected}" /></strong> person(s)</td>
                                            <td><span class="badge ${req.severity.badgeClass}"><c:out value="${req.severity.displayName}" /></span></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty req.assignedTeamName}">
                                                        <span class="badge bg-primary"><i class="bi bi-shield-shaded me-1"></i><c:out value="${req.assignedTeamName}" /></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-warning text-dark"><i class="bi bi-hourglass-split me-1"></i>Queued for Dispatch</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><span class="badge ${req.status.badgeClass}"><c:out value="${req.status.displayName}" /></span></td>
                                            <td>
                                                <small class="text-secondary">
                                                    <c:out value="${not empty req.resolutionNotes ? req.resolutionNotes : 'Operation ongoing.'}" />
                                                </small>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-4 text-muted">
                            <i class="bi bi-check2-circle fs-3 text-success d-block mb-2"></i>
                            You have no active emergency assistance requests.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- 2. My Submitted Disaster Reports -->
        <div class="card card-theme mb-4">
            <div class="card-header">
                <span class="fw-bold"><i class="bi bi-broadcast me-2 text-primary"></i>My Submitted Disaster Reports</span>
                <a href="${pageContext.request.contextPath}/report-disaster" class="btn btn-sm btn-outline-primary">File Another Report</a>
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${not empty myReports}">
                        <div class="table-responsive">
                            <table class="table table-custom table-hover">
                                <thead>
                                    <tr>
                                        <th>Tracking Code</th>
                                        <th>Disaster Type</th>
                                        <th>Title & Details</th>
                                        <th>Severity</th>
                                        <th>Verification Status</th>
                                        <th>Authority Notes</th>
                                        <th>Date Reported</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="rep" items="${myReports}">
                                        <tr>
                                            <td><span class="font-monospace fw-bold text-dark"><c:out value="${rep.code}" /></span></td>
                                            <td><span class="badge ${rep.disasterType.badgeColor}"><c:out value="${rep.disasterType.displayName}" /></span></td>
                                            <td>
                                                <div class="fw-bold text-dark"><c:out value="${rep.title}" /></div>
                                                <div class="text-muted small"><i class="bi bi-geo-alt me-1"></i><c:out value="${rep.location}" /></div>
                                            </td>
                                            <td><span class="badge ${rep.severity.badgeClass}"><c:out value="${rep.severity.displayName}" /></span></td>
                                            <td><span class="badge ${rep.status.badgeClass}"><c:out value="${rep.status.displayName}" /></span></td>
                                            <td><small class="text-secondary"><c:out value="${not empty rep.adminNotes ? rep.adminNotes : 'Under review'}" /></small></td>
                                            <td><small class="text-muted"><c:out value="${rep.incidentDate}" /></small></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-4 text-muted">
                            You have not submitted any disaster reports yet.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- 3. Safe Evacuation Shelters Widget -->
        <div class="card card-theme">
            <div class="card-header">
                <span class="fw-bold"><i class="bi bi-building text-success me-2"></i>Available Safe Shelters in Your District</span>
                <a href="${pageContext.request.contextPath}/shelters" class="btn btn-sm btn-outline-success">Browse All Shelters</a>
            </div>
            <div class="card-body">
                <div class="row g-3">
                    <c:forEach var="shelter" items="${nearbyShelters}">
                        <div class="col-md-6">
                            <div class="border rounded-3 p-3 bg-light">
                                <div class="d-flex justify-content-between align-items-center mb-1">
                                    <h6 class="fw-bold mb-0 text-dark"><c:out value="${shelter.name}" /></h6>
                                    <span class="badge ${shelter.status.badgeClass}"><c:out value="${shelter.status.displayName}" /></span>
                                </div>
                                <div class="text-muted small mb-2"><i class="bi bi-geo-alt me-1 text-danger"></i><c:out value="${shelter.address}" />, <c:out value="${shelter.city}" /></div>
                                <div class="d-flex justify-content-between small text-secondary">
                                    <span>Occupancy: <strong><c:out value="${shelter.currentOccupancy}" /> / <c:out value="${shelter.totalCapacity}" /></strong></span>
                                    <span class="text-success fw-bold"><c:out value="${shelter.availableCapacity}" /> spots free</span>
                                </div>
                                <div class="text-muted small mt-2">
                                    <i class="bi bi-telephone me-1"></i>Helpline: <strong><c:out value="${shelter.contactPhone}" /></strong>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

