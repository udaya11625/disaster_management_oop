<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Rescue Team Operations Dashboard" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <!-- Banner -->
        <div class="p-4 rounded-4 bg-primary text-white shadow-sm mb-4 d-flex justify-content-between align-items-center flex-wrap gap-3">
            <div>
                <span class="badge bg-warning text-dark fw-bold mb-2"><i class="bi bi-shield-shaded me-1"></i>Tactical Rescue Squadron</span>
                <h2 class="fw-bold mb-1"><c:out value="${currentTeam.teamName}" /></h2>
                <p class="mb-0 text-white-50 small">Specialization: <c:out value="${currentTeam.specialization}" /> &bull; Sector: <c:out value="${currentTeam.operationalArea}" /></p>
            </div>
            <!-- Squad Availability Status Form -->
            <form action="${pageContext.request.contextPath}/rescue/mission/update" method="POST" class="d-flex align-items-center gap-2 bg-dark p-2 rounded-3 border border-secondary">
                <input type="hidden" name="teamId" value="${currentTeam.id}">
                <label class="text-white small fw-bold mb-0">Squad Status:</label>
                <select name="availability" class="form-select form-select-sm bg-dark text-white border-secondary" onchange="this.form.submit()">
                    <option value="AVAILABLE" ${currentTeam.availability == 'AVAILABLE' ? 'selected' : ''}>Available for Dispatch</option>
                    <option value="ON_MISSION" ${currentTeam.availability == 'ON_MISSION' ? 'selected' : ''}>On Active Mission</option>
                    <option value="OFF_DUTY" ${currentTeam.availability == 'OFF_DUTY' ? 'selected' : ''}>Off Duty / Standby</option>
                </select>
            </form>
        </div>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <!-- Squad Stats Summary -->
        <div class="row g-4 mb-4">
            <div class="col-md-4">
                <div class="stat-card">
                    <div class="stat-icon bg-danger bg-opacity-10 text-danger">
                        <i class="bi bi-lightning-charge"></i>
                    </div>
                    <div class="stat-value">${assignedMissions.size()}</div>
                    <div class="stat-label">Assigned Missions</div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stat-card">
                    <div class="stat-icon bg-info bg-opacity-10 text-info">
                        <i class="bi bi-people"></i>
                    </div>
                    <div class="stat-value"><c:out value="${currentTeam.memberCount}" /></div>
                    <div class="stat-label">Active Squad Members</div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stat-card">
                    <div class="stat-icon bg-success bg-opacity-10 text-success">
                        <i class="bi bi-telephone"></i>
                    </div>
                    <div class="stat-value fs-5"><c:out value="${currentTeam.contactNumber}" /></div>
                    <div class="stat-label">Tactical Comms Hotline</div>
                </div>
            </div>
        </div>

        <!-- Assigned Rescue Missions Table -->
        <div class="card card-theme mb-4">
            <div class="card-header bg-white">
                <span class="fw-bold fs-6"><i class="bi bi-crosshair text-danger me-2"></i>Assigned Emergency Rescue Operations (${assignedMissions.size()})</span>
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${not empty assignedMissions}">
                        <div class="table-responsive">
                            <table class="table table-custom table-hover align-middle">
                                <thead>
                                    <tr>
                                        <th>Code & Priority</th>
                                        <th>Citizen / Victim Contact</th>
                                        <th>Emergency Location & Landmark</th>
                                        <th>People Affected</th>
                                        <th>Assistance Needed</th>
                                        <th>Special Needs</th>
                                        <th>Current Status</th>
                                        <th>Mission Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="mission" items="${assignedMissions}">
                                        <tr class="${mission.priority == 'CRITICAL' ? 'table-danger' : ''}">
                                            <td>
                                                <span class="font-monospace fw-bold text-dark"><c:out value="${mission.code}" /></span>
                                                <div><span class="badge ${mission.severity.badgeClass}"><c:out value="${mission.severity.displayName}" /></span></div>
                                            </td>
                                            <td>
                                                <div class="fw-bold text-dark"><c:out value="${mission.contactName}" /></div>
                                                <div class="small text-danger fw-semibold"><i class="bi bi-telephone-fill me-1"></i><c:out value="${mission.contactPhone}" /></div>
                                            </td>
                                            <td>
                                                <div class="fw-bold text-dark"><i class="bi bi-geo-alt-fill text-danger me-1"></i><c:out value="${mission.location}" /></div>
                                                <div class="small text-muted"><c:out value="${mission.landmark}" /></div>
                                            </td>
                                            <td><strong><c:out value="${mission.peopleAffected}" /></strong> person(s)</td>
                                            <td><span class="badge bg-secondary"><c:out value="${mission.assistanceType}" /></span></td>
                                            <td>
                                                <small class="text-danger fw-semibold">
                                                    <c:out value="${not empty mission.specialNeeds ? mission.specialNeeds : 'None'}" />
                                                </small>
                                            </td>
                                            <td>
                                                <span class="badge ${mission.status.badgeClass}"><c:out value="${mission.status.displayName}" /></span>
                                            </td>
                                            <td>
                                                <button type="button" class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#updateMissionModal${mission.id}">
                                                    <i class="bi bi-pencil-square me-1"></i>Update Progress
                                                </button>

                                                <!-- Modal for Mission Progress Update -->
                                                <div class="modal fade" id="updateMissionModal${mission.id}" tabindex="-1">
                                                    <div class="modal-dialog">
                                                        <div class="modal-content">
                                                            <form action="${pageContext.request.contextPath}/admin/requests/status" method="POST">
                                                                <input type="hidden" name="requestId" value="${mission.id}">
                                                                <div class="modal-header bg-primary text-white">
                                                                    <h5 class="modal-title fs-6 fw-bold">Update Rescue Operation - <c:out value="${mission.code}" /></h5>
                                                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    <div class="mb-3">
                                                                        <label class="form-label">Mission Operational Status</label>
                                                                        <select name="status" class="form-select">
                                                                            <c:forEach var="st" items="${requestStatuses}">
                                                                                <option value="${st.name()}" ${mission.status == st ? 'selected' : ''}>${st.displayName}</option>
                                                                            </c:forEach>
                                                                        </select>
                                                                    </div>
                                                                    <div class="mb-3">
                                                                        <label class="form-label">Operational Report / Resolution Notes</label>
                                                                        <textarea name="resolutionNotes" class="form-control" rows="3" placeholder="Provide notes on victim extraction, hospital transport, or safety checks..."><c:out value="${mission.resolutionNotes}" /></textarea>
                                                                    </div>
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                                                                    <button type="submit" class="btn btn-success btn-sm fw-bold">Save Mission Progress</button>
                                                                </div>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5 text-muted">
                            <i class="bi bi-shield-check text-success fs-1 mb-2 d-block"></i>
                            <h5>No active missions dispatched to your squad.</h5>
                            <p class="small text-muted mb-0">Stay vigilant on radio comms and standby for emergency coordination.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

