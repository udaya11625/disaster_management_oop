<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="National Disaster Emergency Management System" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main>
    <!-- Critical Flash Bulletins Ticker -->
    <c:if test="${not empty activeUpdates}">
        <div class="emergency-alert-bar">
            <div class="container-fluid px-lg-4 d-flex align-items-center justify-content-between flex-wrap gap-2">
                <div class="d-flex align-items-center">
                    <span class="badge bg-warning text-dark me-2 text-uppercase fw-bold"><i class="bi bi-exclamation-triangle-fill me-1"></i>Active Alert</span>
                    <span class="text-truncate" style="max-width: 800px;">
                        <c:out value="${activeUpdates[0].title}" /> &mdash; <c:out value="${activeUpdates[0].affectedAreas}" />
                    </span>
                </div>
                <a href="${pageContext.request.contextPath}/updates" class="text-white text-decoration-underline small fw-bold">View All Advisories <i class="bi bi-arrow-right"></i></a>
            </div>
        </div>
    </c:if>

    <!-- Hero Section -->
    <section class="hero-wrapper">
        <div class="container">
            <div class="row align-items-center g-5">
                <div class="col-lg-7">
                    <div class="badge bg-danger text-uppercase px-3 py-2 mb-3 fw-bold tracking-wider">
                        <span class="pulse-dot me-1"></span> 24/7 Rapid Emergency Operations Center
                    </div>
                    <h1 class="hero-title">Integrated Disaster Management & Emergency Response</h1>
                    <p class="hero-lead">
                        Real-time coordination for floods, earthquakes, landslides, cyclones, and fires. 
                        Empowering citizens to report incidents and request critical rescue assistance, while mobilizing volunteers and relief inventory.
                    </p>
                    <div class="d-flex gap-3 flex-wrap">
                        <a href="${pageContext.request.contextPath}/request-assistance" class="btn btn-danger btn-lg fw-bold shadow-lg px-4 py-3">
                            <i class="bi bi-telephone-inbound-fill me-2"></i>Request Emergency SOS Aid
                        </a>
                        <a href="${pageContext.request.contextPath}/report-disaster" class="btn btn-outline-light btn-lg fw-semibold px-4 py-3">
                            <i class="bi bi-plus-circle me-2"></i>Report a Disaster
                        </a>
                        <a href="${pageContext.request.contextPath}/shelters" class="btn btn-primary btn-lg fw-semibold px-4 py-3">
                            <i class="bi bi-building me-2"></i>Locate Safe Shelters
                        </a>
                    </div>
                </div>
                <div class="col-lg-5">
                    <div class="card bg-dark bg-opacity-75 text-white border-0 shadow-lg p-4 rounded-4 backdrop-blur">
                        <div class="d-flex align-items-center justify-content-between mb-3 border-bottom border-secondary pb-3">
                            <div class="fw-bold fs-5 text-danger"><i class="bi bi-activity me-2"></i>Incident Command Status</div>
                            <span class="badge bg-success"><i class="bi bi-circle-fill me-1 small"></i>Operational</span>
                        </div>
                        <div class="row g-3">
                            <div class="col-6">
                                <div class="p-3 bg-secondary bg-opacity-25 rounded-3">
                                    <div class="text-muted small">Active Incidents</div>
                                    <div class="fs-3 fw-bold text-warning"><c:out value="${stats.activeIncidents != null ? stats.activeIncidents : 0}" /></div>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="p-3 bg-secondary bg-opacity-25 rounded-3">
                                    <div class="text-muted small">Critical SOS Requests</div>
                                    <div class="fs-3 fw-bold text-danger"><c:out value="${stats.criticalRequests != null ? stats.criticalRequests : 0}" /></div>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="p-3 bg-secondary bg-opacity-25 rounded-3">
                                    <div class="text-muted small">Shelter Capacity</div>
                                    <div class="fs-4 fw-bold text-info"><c:out value="${stats.availableCapacity != null ? stats.availableCapacity : 0}" /> <span class="fs-6 fw-normal text-muted">free</span></div>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="p-3 bg-secondary bg-opacity-25 rounded-3">
                                    <div class="text-muted small">Available Rescue Teams</div>
                                    <div class="fs-4 fw-bold text-success"><c:out value="${stats.availableRescueTeams != null ? stats.availableRescueTeams : 0}" /> <span class="fs-6 fw-normal text-muted">squads</span></div>
                                </div>
                            </div>
                        </div>
                        <div class="mt-4 pt-3 border-top border-secondary text-center">
                            <span class="text-secondary small">Need volunteer mobilization?</span>
                            <a href="${pageContext.request.contextPath}/volunteer-register" class="text-warning fw-bold text-decoration-none ms-1">Register as Volunteer &rarr;</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Main Container -->
    <div class="container my-5">
        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <!-- Core Mission Features Grid -->
        <div class="row g-4 mb-5">
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon bg-primary bg-opacity-10 text-primary">
                        <i class="bi bi-broadcast"></i>
                    </div>
                    <div class="stat-value"><c:out value="${stats.totalReports != null ? stats.totalReports : 0}" /></div>
                    <div class="stat-label">Total Disaster Reports</div>
                    <p class="text-muted small mt-2 mb-0">Citizens reporting flood, fire, and storm emergencies.</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon bg-danger bg-opacity-10 text-danger">
                        <i class="bi bi-life-preserver"></i>
                    </div>
                    <div class="stat-value"><c:out value="${stats.pendingRequests != null ? stats.pendingRequests : 0}" /></div>
                    <div class="stat-label">Pending Assistance SOS</div>
                    <p class="text-muted small mt-2 mb-0">Evacuation and medical aid missions in dispatch queue.</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon bg-info bg-opacity-10 text-info">
                        <i class="bi bi-building-check"></i>
                    </div>
                    <div class="stat-value"><c:out value="${stats.totalShelters != null ? stats.totalShelters : 0}" /></div>
                    <div class="stat-label">Designated Safe Shelters</div>
                    <p class="text-muted small mt-2 mb-0"><c:out value="${stats.shelterOccupancyRate != null ? stats.shelterOccupancyRate : 0}" />% overall occupancy rate.</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <div class="stat-icon bg-success bg-opacity-10 text-success">
                        <i class="bi bi-box2-heart"></i>
                    </div>
                    <div class="stat-value"><c:out value="${stats.totalResourceUnits != null ? stats.totalResourceUnits : 0}" /></div>
                    <div class="stat-label">Relief Supplies Stocked</div>
                    <p class="text-muted small mt-2 mb-0">Food rations, drinking water, first-aid & rescue equipment.</p>
                </div>
            </div>
        </div>

        <!-- Recent Disaster Reports & Live Shelters Grid -->
        <div class="row g-4 mb-5">
            <!-- Left: Recent Disaster Reports -->
            <div class="col-lg-7">
                <div class="card card-theme h-100">
                    <div class="card-header">
                        <div class="d-flex align-items-center">
                            <i class="bi bi-exclamation-octagon-fill text-danger me-2 fs-5"></i>
                            <span class="fs-6 fw-bold">Recent Verified Disaster Incidents</span>
                        </div>
                        <a href="${pageContext.request.contextPath}/reports" class="btn btn-sm btn-outline-primary">View All Reports</a>
                    </div>
                    <div class="card-body p-0">
                        <c:choose>
                            <c:when test="${not empty recentReports}">
                                <div class="list-group list-group-flush">
                                    <c:forEach var="report" items="${recentReports}">
                                        <div class="list-group-item p-3">
                                            <div class="d-flex justify-content-between align-items-start mb-1">
                                                <div>
                                                    <span class="badge ${report.disasterType.badgeColor} me-1">
                                                        <i class="bi ${report.disasterType.iconClass} me-1"></i><c:out value="${report.disasterType.displayName}" />
                                                    </span>
                                                    <span class="badge ${report.severity.badgeClass} me-1"><c:out value="${report.severity.displayName}" /></span>
                                                    <span class="badge ${report.status.badgeClass}"><c:out value="${report.status.displayName}" /></span>
                                                </div>
                                                <small class="text-muted font-monospace"><c:out value="${report.code}" /></small>
                                            </div>
                                            <h6 class="mb-1 text-dark fw-bold"><c:out value="${report.title}" /></h6>
                                            <p class="mb-2 text-secondary small text-truncate"><c:out value="${report.description}" /></p>
                                            <div class="d-flex justify-content-between text-muted small">
                                                <span><i class="bi bi-geo-alt-fill text-danger me-1"></i><c:out value="${report.location}" /></span>
                                                <span><i class="bi bi-clock me-1"></i><c:out value="${report.incidentDate}" /></span>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="p-4 text-center text-muted">No recent disaster reports recorded.</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- Right: Available Shelters Preview -->
            <div class="col-lg-5">
                <div class="card card-theme h-100">
                    <div class="card-header">
                        <div class="d-flex align-items-center">
                            <i class="bi bi-shield-check text-success me-2 fs-5"></i>
                            <span class="fs-6 fw-bold">Active Evacuation Shelters</span>
                        </div>
                        <a href="${pageContext.request.contextPath}/shelters" class="btn btn-sm btn-outline-success">Browse All</a>
                    </div>
                    <div class="card-body p-0">
                        <c:choose>
                            <c:when test="${not empty activeShelters}">
                                <div class="list-group list-group-flush">
                                    <c:forEach var="shelter" items="${activeShelters}">
                                        <div class="list-group-item p-3">
                                            <div class="d-flex justify-content-between align-items-center mb-1">
                                                <h6 class="mb-0 fw-bold text-dark"><c:out value="${shelter.name}" /></h6>
                                                <span class="badge ${shelter.status.badgeClass}"><c:out value="${shelter.status.displayName}" /></span>
                                            </div>
                                            <div class="text-muted small mb-2"><i class="bi bi-geo-alt me-1"></i><c:out value="${shelter.address}" />, <c:out value="${shelter.city}" /></div>
                                            <div class="d-flex justify-content-between text-muted small mb-1">
                                                <span>Occupancy: <strong><c:out value="${shelter.currentOccupancy}" /></strong> / <c:out value="${shelter.totalCapacity}" /></span>
                                                <span class="text-success fw-bold"><c:out value="${shelter.availableCapacity}" /> spots free</span>
                                            </div>
                                            <div class="progress" style="height: 6px;">
                                                <div class="progress-bar ${shelter.occupancyRate > 90 ? 'bg-danger' : shelter.occupancyRate > 60 ? 'bg-warning' : 'bg-success'}" 
                                                     role="progressbar" style="width: ${shelter.occupancyRate}%;"></div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="p-4 text-center text-muted">No shelters currently listed.</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <!-- Emergency Assistance Callout Banner -->
        <div class="p-4 rounded-4 bg-danger text-white shadow-lg d-flex justify-content-between align-items-center flex-wrap gap-3">
            <div>
                <h4 class="fw-bold mb-1"><i class="bi bi-heart-pulse-fill me-2"></i>Trapped or Need Immediate Evacuation Assistance?</h4>
                <p class="mb-0 text-white-50">Submit an emergency SOS request directly to dispatched disaster rescue teams and field volunteers.</p>
            </div>
            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/request-assistance" class="btn btn-light text-danger fw-bold btn-lg">
                    <i class="bi bi-send-fill me-1"></i>Request Emergency Aid
                </a>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
