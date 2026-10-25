<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Volunteer Operations Dashboard" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <!-- Header Banner -->
        <div class="p-4 rounded-4 bg-success text-white shadow-sm mb-4 d-flex justify-content-between align-items-center flex-wrap gap-3">
            <div>
                <span class="badge bg-white text-success fw-bold mb-2"><i class="bi bi-heart-fill me-1"></i>Volunteer Response Hub</span>
                <h2 class="fw-bold mb-1"><c:out value="${sessionScope.currentUser.fullName}" /></h2>
                <p class="mb-0 text-white-50 small">Community Volunteer Corps &bull; Field Operations & Relief Coordination</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-light btn-sm fw-semibold">
                    <i class="bi bi-person-gear me-1"></i>Update Profile & Skills
                </a>
            </div>
        </div>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <!-- Profile & Skills Card -->
        <div class="card card-theme mb-4">
            <div class="card-body p-4">
                <div class="row g-4 align-items-center">
                    <div class="col-md-3 border-end">
                        <div class="text-muted small">Current Operational Status</div>
                        <div class="fs-5 fw-bold text-success mt-1">
                            <span class="badge bg-success px-3 py-2 fs-6">
                                <i class="bi bi-check-circle-fill me-1"></i>Active / Available for Dispatch
                            </span>
                        </div>
                    </div>
                    <div class="col-md-5 border-end">
                        <div class="text-muted small">Registered Skills & Certifications</div>
                        <div class="fw-semibold text-dark mt-1">
                            <c:out value="${sessionScope.currentUser.skills != null ? sessionScope.currentUser.skills : 'Emergency First Response, Community Support'}" />
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="text-muted small">Assignment Preferences & Location</div>
                        <div class="fw-semibold text-dark mt-1">
                            <i class="bi bi-geo-alt-fill text-danger me-1"></i><c:out value="${sessionScope.currentUser.location != null ? sessionScope.currentUser.location : 'Metro Operational Area'}" />
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Assigned Tasks Table -->
        <div class="card card-theme mb-4">
            <div class="card-header bg-white">
                <span class="fw-bold fs-6"><i class="bi bi-list-check text-primary me-2"></i>My Field Assignments & Disaster Relief Tasks (${myAssignments.size()})</span>
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${not empty myAssignments}">
                        <div class="table-responsive">
                            <table class="table table-custom table-hover align-middle">
                                <thead>
                                    <tr>
                                        <th>Task Details</th>
                                        <th>Linked Incident</th>
                                        <th>Assigned Timestamp</th>
                                        <th>Status</th>
                                        <th>Update Progress</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="task" items="${myAssignments}">
                                        <tr>
                                            <td>
                                                <div class="fw-bold text-dark"><c:out value="${task.taskTitle}" /></div>
                                                <div class="text-muted small"><c:out value="${task.taskDescription}" /></div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty task.reportTitle}">
                                                        <span class="badge bg-secondary"><i class="bi bi-broadcast me-1"></i>Report: <c:out value="${task.reportTitle}" /></span>
                                                    </c:when>
                                                    <c:when test="${not empty task.requestLocation}">
                                                        <span class="badge bg-danger"><i class="bi bi-geo-alt me-1"></i>SOS Aid: <c:out value="${task.requestLocation}" /></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted small">General Community Task</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><small class="text-muted"><c:out value="${task.assignedAt}" /></small></td>
                                            <td>
                                                <span class="badge ${task.status == 'COMPLETED' ? 'bg-success' : task.status == 'IN_PROGRESS' ? 'bg-primary' : 'bg-warning text-dark'}">
                                                    <c:out value="${task.status}" />
                                                </span>
                                            </td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/volunteer/tasks/update" method="POST" class="d-flex gap-2">
                                                    <input type="hidden" name="assignmentId" value="${task.id}">
                                                    <c:if test="${task.status == 'ASSIGNED'}">
                                                        <button type="submit" name="status" value="IN_PROGRESS" class="btn btn-sm btn-primary">
                                                            <i class="bi bi-play-fill me-1"></i>Start
                                                        </button>
                                                    </c:if>
                                                    <c:if test="${task.status != 'COMPLETED'}">
                                                        <button type="submit" name="status" value="COMPLETED" class="btn btn-sm btn-success">
                                                            <i class="bi bi-check-lg me-1"></i>Mark Completed
                                                        </button>
                                                    </c:if>
                                                    <c:if test="${task.status == 'COMPLETED'}">
                                                        <span class="badge bg-light text-success border"><i class="bi bi-check-circle me-1"></i>Finished</span>
                                                    </c:if>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5 text-muted">
                            <i class="bi bi-clipboard-check text-success fs-1 mb-2 d-block"></i>
                            <h5>No active tasks assigned to you right now.</h5>
                            <p class="small text-muted mb-0">Stay on standby. The Disaster Control Center will notify you when volunteers are deployed.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Live Emergency Bulletins -->
        <div class="card card-theme">
            <div class="card-header bg-white">
                <span class="fw-bold fs-6"><i class="bi bi-megaphone-fill text-danger me-2"></i>Active Operational Safety Bulletins</span>
            </div>
            <div class="card-body">
                <c:forEach var="adv" items="${activeAdvisories}">
                    <div class="p-3 mb-2 rounded bg-light border-start border-4 ${adv.severity == 'CRITICAL' ? 'border-danger' : 'border-warning'}">
                        <div class="d-flex justify-content-between">
                            <h6 class="fw-bold mb-1"><c:out value="${adv.title}" /></h6>
                            <small class="text-muted"><c:out value="${adv.publishedAt}" /></small>
                        </div>
                        <p class="text-secondary small mb-1"><c:out value="${adv.content}" /></p>
                        <span class="badge bg-secondary small"><i class="bi bi-geo-alt me-1"></i><c:out value="${adv.affectedAreas}" /></span>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
