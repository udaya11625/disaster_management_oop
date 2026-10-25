<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Public Disaster Reports Feed" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <!-- Page Title & Actions -->
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-4">
            <div>
                <h1 class="fw-bold text-dark mb-1"><i class="bi bi-broadcast text-danger me-2"></i>Disaster Reports Directory</h1>
                <p class="text-muted mb-0">Live community incident reports submitted by citizens and verified by emergency authorities.</p>
            </div>
            <a href="${pageContext.request.contextPath}/report-disaster" class="btn btn-danger fw-bold shadow-sm">
                <i class="bi bi-plus-circle-fill me-1"></i>Report a Disaster
            </a>
        </div>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <!-- Search & Filter Card -->
        <div class="card card-theme mb-4">
            <div class="card-body p-4">
                <form action="${pageContext.request.contextPath}/reports" method="GET" class="row g-3 align-items-end">
                    <div class="col-md-4">
                        <label class="form-label small">Search Keyword</label>
                        <div class="input-group">
                            <span class="input-group-text bg-light"><i class="bi bi-search"></i></span>
                            <input type="text" name="keyword" class="form-control" placeholder="Search by title, location, or code..." value="<c:out value='${pKeyword}' />">
                        </div>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label small">Disaster Type</label>
                        <select name="type" class="form-select">
                            <option value="ALL">All Disaster Types</option>
                            <c:forEach var="t" items="${disasterTypes}">
                                <option value="${t.name()}" ${pType == t.name() ? 'selected' : ''}>${t.displayName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label small">Severity Level</label>
                        <select name="severity" class="form-select">
                            <option value="ALL">All Severities</option>
                            <c:forEach var="s" items="${severities}">
                                <option value="${s.name()}" ${pSeverity == s.name() ? 'selected' : ''}>${s.displayName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label small">Report Status</label>
                        <select name="status" class="form-select">
                            <option value="ALL">All Statuses</option>
                            <c:forEach var="st" items="${statuses}">
                                <option value="${st.name()}" ${pStatus == st.name() ? 'selected' : ''}>${st.displayName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2 d-flex gap-2">
                        <button type="submit" class="btn btn-primary w-100 fw-semibold"><i class="bi bi-filter me-1"></i>Filter</button>
                        <a href="${pageContext.request.contextPath}/reports" class="btn btn-outline-secondary" title="Reset Filters"><i class="bi bi-arrow-counterclockwise"></i></a>
                    </div>
                </form>
            </div>
        </div>

        <!-- Reports Grid / Table -->
        <div class="card card-theme">
            <div class="card-header">
                <span class="fw-bold"><i class="bi bi-list-columns-reverse me-2 text-primary"></i>Logged Incident Reports (${reports.size()} found)</span>
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${not empty reports}">
                        <div class="table-responsive">
                            <table class="table table-custom table-hover align-middle">
                                <thead>
                                    <tr>
                                        <th>Tracking Code</th>
                                        <th>Disaster Type</th>
                                        <th>Incident Title & Location</th>
                                        <th>Severity</th>
                                        <th>Status</th>
                                        <th>Reported Time</th>
                                        <th>Evidence</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="rep" items="${reports}">
                                        <tr>
                                            <td>
                                                <span class="font-monospace fw-bold text-dark"><c:out value="${rep.code}" /></span>
                                                <c:if test="${not empty rep.reporterName}">
                                                    <div class="small text-muted">by <c:out value="${rep.reporterName}" /></div>
                                                </c:if>
                                            </td>
                                            <td>
                                                <span class="badge ${rep.disasterType.badgeColor}">
                                                    <i class="bi ${rep.disasterType.iconClass} me-1"></i><c:out value="${rep.disasterType.displayName}" />
                                                </span>
                                            </td>
                                            <td>
                                                <div class="fw-bold text-dark"><c:out value="${rep.title}" /></div>
                                                <div class="text-secondary small text-truncate" style="max-width: 380px;">
                                                    <c:out value="${rep.description}" />
                                                </div>
                                                <div class="text-muted small mt-1">
                                                    <i class="bi bi-geo-alt-fill text-danger me-1"></i><c:out value="${rep.location}" />
                                                    <c:if test="${not empty rep.landmark}">
                                                        &bull; <em>(<c:out value="${rep.landmark}" />)</em>
                                                    </c:if>
                                                </div>
                                            </td>
                                            <td>
                                                <span class="badge ${rep.severity.badgeClass}">
                                                    <c:out value="${rep.severity.displayName}" />
                                                </span>
                                            </td>
                                            <td>
                                                <span class="badge ${rep.status.badgeClass}">
                                                    <c:out value="${rep.status.displayName}" />
                                                </span>
                                                <c:if test="${not empty rep.adminNotes}">
                                                    <i class="bi bi-info-circle ms-1 text-muted" data-bs-toggle="tooltip" title="Note: ${rep.adminNotes}"></i>
                                                </c:if>
                                            </td>
                                            <td>
                                                <div class="small text-dark fw-semibold"><c:out value="${rep.incidentDate}" /></div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty rep.imageUrl}">
                                                        <a href="<c:out value='${rep.imageUrl}' />" target="_blank" class="btn btn-sm btn-outline-info" title="View Incident Photo">
                                                            <i class="bi bi-image me-1"></i>Photo
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted small">&mdash;</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5">
                            <i class="bi bi-clipboard-x text-muted fs-1 mb-3 d-block"></i>
                            <h5 class="text-muted">No disaster reports match the selected filters.</h5>
                            <a href="${pageContext.request.contextPath}/reports" class="btn btn-sm btn-outline-primary mt-2">Clear All Filters</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

