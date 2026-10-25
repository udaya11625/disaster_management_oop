<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Broadcast Disaster Advisories & Bulletins" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5 bg-light flex-grow-1">
    <div class="container-fluid px-lg-5">
        <!-- Title Bar -->
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-4">
            <div>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="small text-muted text-decoration-none">
                    <i class="bi bi-arrow-left me-1"></i>Back to Command Dashboard
                </a>
                <h1 class="fw-bold text-dark mb-0"><i class="bi bi-broadcast text-danger me-2"></i>Emergency Bulletins & Public Advisories</h1>
                <p class="text-muted small mb-0">Publish official disaster warnings, weather alerts, and evacuation protocols directly to citizens.</p>
            </div>
            <div>
                <button type="button" class="btn btn-danger fw-bold shadow-sm" data-bs-toggle="modal" data-bs-target="#createAdvisoryModal">
                    <i class="bi bi-megaphone-fill me-1"></i>Broadcast New Advisory
                </button>
            </div>
        </div>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <!-- Broadcasted Advisories Table -->
        <div class="card card-theme">
            <div class="card-header bg-white d-flex justify-content-between align-items-center">
                <span class="fw-bold fs-6"><i class="bi bi-list-ul text-primary me-2"></i>Published Bulletins (${updates.size()})</span>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-custom table-hover align-middle">
                        <thead>
                            <tr>
                                <th>Severity</th>
                                <th>Disaster Hazard</th>
                                <th>Advisory Headline</th>
                                <th>Target Areas</th>
                                <th>Published Timestamp</th>
                                <th>Issuer</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="adv" items="${updates}">
                                <tr>
                                    <td>${adv.severityBadge}</td>
                                    <td>
                                        <span class="badge ${adv.disasterType.badgeColor}">
                                            <i class="bi ${adv.disasterType.iconClass} me-1"></i><c:out value="${adv.disasterType.displayName}" />
                                        </span>
                                    </td>
                                    <td>
                                        <div class="fw-bold text-dark"><c:out value="${adv.title}" /></div>
                                        <div class="small text-secondary text-truncate" style="max-width: 380px;"><c:out value="${adv.content}" /></div>
                                    </td>
                                    <td><i class="bi bi-geo-alt-fill text-danger me-1"></i><c:out value="${adv.affectedAreas}" /></td>
                                    <td><small class="text-muted"><c:out value="${adv.publishedAt}" /></small></td>
                                    <td><small class="text-dark fw-semibold"><c:out value="${not empty adv.issuerName ? adv.issuerName : 'Disaster Director'}" /></small></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/updates/delete" method="POST" onsubmit="return confirm('Archive or deactivate this advisory?');">
                                            <input type="hidden" name="updateId" value="${adv.id}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger" title="Remove Bulletin">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Broadcast New Advisory Modal -->
        <div class="modal fade" id="createAdvisoryModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/admin/updates/add" method="POST">
                        <div class="modal-header bg-danger text-white">
                            <h5 class="modal-title fs-6 fw-bold">Broadcast Official Disaster Advisory</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label class="form-label">Advisory Headline Title <span class="text-danger">*</span></label>
                                <input type="text" name="title" class="form-control" placeholder="e.g. FLASH FLOOD EMERGENCY: Riverdale Basin" required>
                            </div>
                            <div class="row g-2 mb-3">
                                <div class="col-6">
                                    <label class="form-label">Disaster Hazard <span class="text-danger">*</span></label>
                                    <select name="disasterType" class="form-select" required>
                                        <c:forEach var="dt" items="${disasterTypes}">
                                            <option value="${dt.name()}">${dt.displayName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-6">
                                    <label class="form-label">Warning Level <span class="text-danger">*</span></label>
                                    <select name="severity" class="form-select" required>
                                        <option value="CRITICAL" class="text-danger fw-bold">CRITICAL WARNING</option>
                                        <option value="WARNING" selected class="text-warning">SAFETY ADVISORY</option>
                                        <option value="INFORMATION" class="text-info">INFORMATIONAL BULLETIN</option>
                                    </select>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Affected Geographical Regions <span class="text-danger">*</span></label>
                                <input type="text" name="affectedAreas" class="form-control" placeholder="e.g. Sectors 1-6, Lowland Valley, Highway 9" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Official Advisory Message & Instructions <span class="text-danger">*</span></label>
                                <textarea name="content" class="form-control" rows="4" placeholder="Detail the danger, mandatory evacuation routes, emergency shelter locations, and emergency contacts..." required></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-danger btn-sm fw-bold">Broadcast Immediately</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
