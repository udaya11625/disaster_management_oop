<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Official Emergency Bulletins & Advisories" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <!-- Header -->
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-4">
            <div>
                <h1 class="fw-bold text-dark mb-1"><i class="bi bi-megaphone-fill text-warning me-2"></i>Official Disaster Updates & Advisories</h1>
                <p class="text-muted mb-0">Authorized public warnings, weather bulletins, and evacuation notices issued by the Disaster Control Center.</p>
            </div>
            <c:if test="${sessionScope.currentUser != null && sessionScope.currentUser.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/admin/updates" class="btn btn-danger fw-bold">
                    <i class="bi bi-broadcast me-1"></i>Broadcast New Advisory
                </a>
            </c:if>
        </div>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <!-- Bulletins List -->
        <div class="row g-4">
            <c:choose>
                <c:when test="${not empty updates}">
                    <c:forEach var="upd" items="${updates}">
                        <div class="col-12">
                            <div class="card card-theme border-start border-4 ${upd.severity == 'CRITICAL' ? 'border-danger' : upd.severity == 'WARNING' ? 'border-warning' : 'border-info'} shadow-sm">
                                <div class="card-body p-4">
                                    <div class="d-flex justify-content-between align-items-start flex-wrap gap-2 mb-2">
                                        <div class="d-flex align-items-center gap-2">
                                            <span class="badge ${upd.disasterType.badgeColor}">
                                                <i class="bi ${upd.disasterType.iconClass} me-1"></i><c:out value="${upd.disasterType.displayName}" />
                                            </span>
                                            ${upd.severityBadge}
                                        </div>
                                        <small class="text-muted"><i class="bi bi-clock me-1"></i>Published: <c:out value="${upd.publishedAt}" /></small>
                                    </div>

                                    <h4 class="fw-bold text-dark mb-2"><c:out value="${upd.title}" /></h4>
                                    <div class="text-muted small mb-3">
                                        <i class="bi bi-geo-alt-fill text-danger me-1"></i><strong>Target / Affected Areas:</strong> <c:out value="${upd.affectedAreas}" />
                                    </div>
                                    <p class="text-secondary mb-3 fs-6">${upd.content}</p>

                                    <div class="d-flex justify-content-between align-items-center pt-2 border-top text-muted small">
                                        <span><i class="bi bi-shield-check text-primary me-1"></i>Issued by: <strong><c:out value="${not empty upd.issuerName ? upd.issuerName : 'Emergency Director'}" /></strong></span>
                                        <span class="badge bg-light text-secondary border">National Emergency Center</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12 text-center py-5">
                        <i class="bi bi-bell-slash text-muted fs-1 mb-3 d-block"></i>
                        <h5 class="text-muted">No active emergency bulletins at this time.</h5>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
