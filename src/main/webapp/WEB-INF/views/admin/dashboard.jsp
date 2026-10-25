<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Administrator Command Center" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5 bg-light flex-grow-1">
    <div class="container-fluid px-lg-5">
        <!-- Top Title Bar -->
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-4">
            <div>
                <span class="badge bg-danger text-uppercase px-3 py-1 mb-1 fw-bold">Emergency Operations Command</span>
                <h1 class="fw-bold text-dark mb-0">National Disaster Control Dashboard</h1>
                <p class="text-muted small mb-0">Centralized coordination: incident reporting, rescue squad dispatch, volunteer deployment, and relief logistics.</p>
            </div>
            <div class="d-flex gap-2 flex-wrap">
                <a href="${pageContext.request.contextPath}/admin/shelters" class="btn btn-primary fw-semibold btn-sm shadow-sm">
                    <i class="bi bi-door-open me-1"></i>Manage Shelters
                </a>
                <a href="${pageContext.request.contextPath}/admin/resources" class="btn btn-success fw-semibold btn-sm shadow-sm">
                    <i class="bi bi-boxes me-1"></i>Relief Supplies
                </a>
                <a href="${pageContext.request.contextPath}/admin/updates" class="btn btn-warning text-dark fw-bold btn-sm shadow-sm">
                    <i class="bi bi-broadcast me-1"></i>Broadcast Advisory
                </a>
            </div>
        </div>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <!-- Database Metrics Stat Cards -->
        <div class="row g-3 mb-4">
            <div class="col-xl-2 col-md-4 col-sm-6">
                <div class="stat-card border-top border-4 border-primary">
                    <div class="stat-value text-primary"><c:out value="${stats.totalReports != null ? stats.totalReports : 0}" /></div>
                    <div class="stat-label">Total Reports</div>
                    <div class="small text-muted mt-1"><c:out value="${stats.activeIncidents != null ? stats.activeIncidents : 0}" /> active incidents</div>
                </div>
            </div>
            <div class="col-xl-2 col-md-4 col-sm-6">
                <div class="stat-card border-top border-4 border-danger">
                    <div class="stat-value text-danger"><c:out value="${stats.criticalRequests != null ? stats.criticalRequests : 0}" /></div>
                    <div class="stat-label">Critical SOS Calls</div>
                    <div class="small text-danger fw-semibold mt-1"><c:out value="${stats.pendingRequests != null ? stats.pendingRequests : 0}" /> pending dispatch</div>
                </div>
            </div>
            <div class="col-xl-2 col-md-4 col-sm-6">
                <div class="stat-card border-top border-4 border-info">
                    <div class="stat-value text-info"><c:out value="${stats.availableCapacity != null ? stats.availableCapacity : 0}" /></div>
                    <div class="stat-label">Free Shelter Spots</div>
                    <div class="small text-muted mt-1"><c:out value="${stats.shelterOccupancyRate != null ? stats.shelterOccupancyRate : 0}" />% total occupancy</div>
                </div>
            </div>
            <div class="col-xl-2 col-md-4 col-sm-6">
                <div class="stat-card border-top border-4 border-success">
                    <div class="stat-value text-success"><c:out value="${stats.availableVolunteers != null ? stats.availableVolunteers : 0}" /></div>
                    <div class="stat-label">Available Volunteers</div>
                    <div class="small text-muted mt-1"><c:out value="${stats.totalVolunteers != null ? stats.totalVolunteers : 0}" /> enrolled</div>
                </div>
            </div>
            <div class="col-xl-2 col-md-4 col-sm-6">
                <div class="stat-card border-top border-4 border-warning">
                    <div class="stat-value text-dark"><c:out value="${stats.availableRescueTeams != null ? stats.availableRescueTeams : 0}" /></div>
                    <div class="stat-label">Ready Rescue Squads</div>
                    <div class="small text-muted mt-1"><c:out value="${stats.totalRescueTeams != null ? stats.totalRescueTeams : 0}" /> squads deployed</div>
                </div>
            </div>
            <div class="col-xl-2 col-md-4 col-sm-6">
                <div class="stat-card border-top border-4 border-secondary">
                    <div class="stat-value text-secondary"><c:out value="${stats.lowStockCount != null ? stats.lowStockCount : 0}" /></div>
                    <div class="stat-label">Low Stock Alerts</div>
                    <div class="small text-danger fw-semibold mt-1"><c:out value="${stats.totalResourceUnits != null ? stats.totalResourceUnits : 0}" /> units stocked</div>
                </div>
            </div>
        </div>

        <!-- Chart.js Visualization Row -->
        <div class="row g-4 mb-4">
            <div class="col-lg-4">
                <div class="card card-theme h-100">
                    <div class="card-header bg-white">
                        <span class="fw-bold"><i class="bi bi-pie-chart-fill text-primary me-2"></i>Reports by Disaster Type</span>
                    </div>
                    <div class="card-body" style="height: 280px; position: relative;">
                        <canvas id="reportsByTypeChart"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="card card-theme h-100">
                    <div class="card-header bg-white">
                        <span class="fw-bold"><i class="bi bi-bar-chart-fill text-danger me-2"></i>SOS Requests by Status</span>
                    </div>
                    <div class="card-body" style="height: 280px; position: relative;">
                        <canvas id="requestsByStatusChart"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="card card-theme h-100">
                    <div class="card-header bg-white">
                        <span class="fw-bold"><i class="bi bi-building-fill text-success me-2"></i>Shelter Capacity vs Occupancy</span>
                    </div>
                    <div class="card-body" style="height: 280px; position: relative;">
                        <canvas id="shelterOccupancyChart"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <!-- Critical Emergency Assistance Requests Section -->
        <div class="card card-theme mb-4">
            <div class="card-header bg-danger text-white d-flex justify-content-between align-items-center">
                <span class="fw-bold fs-6"><i class="bi bi-exclamation-triangle-fill me-2"></i>Priority Emergency SOS Requests (Dispatch Management)</span>
                <span class="badge bg-white text-danger fw-bold">${requests.size()} Requests Logged</span>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-custom table-hover align-middle">
                        <thead>
                            <tr>
                                <th>Code</th>
                                <th>Priority</th>
                                <th>Victim Contact</th>
                                <th>Disaster & Location</th>
                                <th>People Affected</th>
                                <th>Aid Requested</th>
                                <th>Assigned Rescue Squad</th>
                                <th>Status</th>
                                <th>Dispatch Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="req" items="${requests}">
                                <tr class="${req.priority == 'CRITICAL' && req.status != 'RESOLVED' ? 'table-danger' : ''}">
                                    <td><span class="font-monospace fw-bold text-dark"><c:out value="${req.code}" /></span></td>
                                    <td><span class="badge ${req.severity.badgeClass}"><c:out value="${req.severity.displayName}" /></span></td>
                                    <td>
                                        <div class="fw-bold text-dark"><c:out value="${req.contactName}" /></div>
                                        <div class="small text-danger fw-semibold"><i class="bi bi-telephone-fill me-1"></i><c:out value="${req.contactPhone}" /></div>
                                    </td>
                                    <td>
                                        <span class="badge ${req.disasterType.badgeColor} me-1"><c:out value="${req.disasterType.displayName}" /></span>
                                        <div class="small text-dark mt-1"><i class="bi bi-geo-alt-fill text-danger me-1"></i><c:out value="${req.location}" /></div>
                                        <c:if test="${not empty req.landmark}">
                                            <div class="small text-muted"><em>(<c:out value="${req.landmark}" />)</em></div>
                                        </c:if>
                                    </td>
                                    <td><strong><c:out value="${req.peopleAffected}" /></strong> person(s)</td>
                                    <td>
                                        <span class="badge bg-secondary"><c:out value="${req.assistanceType}" /></span>
                                        <c:if test="${not empty req.specialNeeds}">
                                            <div class="small text-danger mt-1">Special: <c:out value="${req.specialNeeds}" /></div>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty req.assignedTeamName}">
                                                <span class="badge bg-primary"><i class="bi bi-shield-shaded me-1"></i><c:out value="${req.assignedTeamName}" /></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning text-dark"><i class="bi bi-hourglass me-1"></i>Unassigned</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><span class="badge ${req.status.badgeClass}"><c:out value="${req.status.displayName}" /></span></td>
                                    <td>
                                        <div class="d-flex gap-1 flex-wrap">
                                            <!-- Assign Team Modal Button -->
                                            <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#assignTeamModal${req.id}" title="Assign Squad">
                                                <i class="bi bi-shield-plus"></i> Assign
                                            </button>
                                            <!-- Update Status Modal Button -->
                                            <button type="button" class="btn btn-sm btn-outline-success" data-bs-toggle="modal" data-bs-target="#updateReqStatusModal${req.id}" title="Update Status">
                                                <i class="bi bi-check2-circle"></i> Status
                                            </button>
                                        </div>

                                        <!-- Assign Team Modal -->
                                        <div class="modal fade" id="assignTeamModal${req.id}" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form action="${pageContext.request.contextPath}/admin/requests/assign" method="POST">
                                                        <input type="hidden" name="requestId" value="${req.id}">
                                                        <div class="modal-header bg-primary text-white">
                                                            <h5 class="modal-title fs-6 fw-bold">Dispatch Rescue Squad to ${req.code}</h5>
                                                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <div class="p-2 mb-3 bg-light rounded small">
                                                                <div><strong>Victim:</strong> ${req.contactName} (${req.contactPhone})</div>
                                                                <div><strong>Location:</strong> ${req.location}</div>
                                                                <div><strong>Disaster:</strong> ${req.disasterType.displayName} | <strong>Priority:</strong> ${req.severity.displayName}</div>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Select Available Rescue Squad</label>
                                                                <select name="teamId" class="form-select" required>
                                                                    <c:forEach var="team" items="${rescueTeams}">
                                                                        <option value="${team.id}" ${team.id == req.assignedTeamId ? 'selected' : ''}>
                                                                            ${team.teamName} (${team.specialization}) - [${team.availability.displayName}]
                                                                        </option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-primary btn-sm fw-bold">Confirm Squad Dispatch</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Update Request Status Modal -->
                                        <div class="modal fade" id="updateReqStatusModal${req.id}" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form action="${pageContext.request.contextPath}/admin/requests/status" method="POST">
                                                        <input type="hidden" name="requestId" value="${req.id}">
                                                        <div class="modal-header bg-dark text-white">
                                                            <h5 class="modal-title fs-6 fw-bold">Update Status - ${req.code}</h5>
                                                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <div class="mb-3">
                                                                <label class="form-label">New Status</label>
                                                                <select name="status" class="form-select">
                                                                    <c:forEach var="st" items="${requestStatuses}">
                                                                        <option value="${st.name()}" ${req.status == st ? 'selected' : ''}>${st.displayName}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Resolution / Action Notes</label>
                                                                <textarea name="resolutionNotes" class="form-control" rows="3" placeholder="Notes on rescue progress or confirmation...">${req.resolutionNotes}</textarea>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-success btn-sm fw-bold">Save Status</button>
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
            </div>
        </div>

        <!-- Disaster Reports Management Table -->
        <div class="card card-theme mb-4">
            <div class="card-header bg-white d-flex justify-content-between align-items-center">
                <span class="fw-bold fs-6"><i class="bi bi-broadcast text-primary me-2"></i>Disaster Reports Verification & Status</span>
                <a href="${pageContext.request.contextPath}/reports" class="btn btn-sm btn-outline-secondary">View Public Feed</a>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-custom table-hover align-middle">
                        <thead>
                            <tr>
                                <th>Report Code</th>
                                <th>Disaster Type</th>
                                <th>Title & Summary</th>
                                <th>Location</th>
                                <th>Severity</th>
                                <th>Verification Status</th>
                                <th>Admin Notes</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="report" items="${reports}">
                                <tr>
                                    <td><span class="font-monospace fw-bold text-dark"><c:out value="${report.code}" /></span></td>
                                    <td><span class="badge ${report.disasterType.badgeColor}"><c:out value="${report.disasterType.displayName}" /></span></td>
                                    <td>
                                        <div class="fw-bold text-dark"><c:out value="${report.title}" /></div>
                                        <div class="text-secondary small text-truncate" style="max-width: 300px;"><c:out value="${report.description}" /></div>
                                    </td>
                                    <td><i class="bi bi-geo-alt-fill text-danger me-1"></i><c:out value="${report.location}" /></td>
                                    <td><span class="badge ${report.severity.badgeClass}"><c:out value="${report.severity.displayName}" /></span></td>
                                    <td><span class="badge ${report.status.badgeClass}"><c:out value="${report.status.displayName}" /></span></td>
                                    <td><small class="text-secondary"><c:out value="${not empty report.adminNotes ? report.adminNotes : 'No notes'}" /></small></td>
                                    <td>
                                        <button type="button" class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#verifyReportModal${report.id}">
                                            <i class="bi bi-pencil-square me-1"></i>Update
                                        </button>

                                        <!-- Verify Modal -->
                                        <div class="modal fade" id="verifyReportModal${report.id}" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form action="${pageContext.request.contextPath}/admin/reports/update-status" method="POST">
                                                        <input type="hidden" name="reportId" value="${report.id}">
                                                        <div class="modal-header bg-dark text-white">
                                                            <h5 class="modal-title fs-6 fw-bold">Update Verification - ${report.code}</h5>
                                                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <div class="mb-3">
                                                                <label class="form-label">Report Status</label>
                                                                <select name="status" class="form-select">
                                                                    <c:forEach var="rst" items="${reportStatuses}">
                                                                        <option value="${rst.name()}" ${report.status == rst ? 'selected' : ''}>${rst.displayName}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Official Authority Notes</label>
                                                                <textarea name="adminNotes" class="form-control" rows="3" placeholder="Enter findings, verification details, or deployment notes...">${report.adminNotes}</textarea>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-primary btn-sm fw-bold">Save Status Update</button>
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
            </div>
        </div>

        <!-- Volunteers & Rescue Teams Management Grid -->
        <div class="row g-4 mb-4">
            <!-- Volunteers List with Assignment Action -->
            <div class="col-lg-6">
                <div class="card card-theme h-100">
                    <div class="card-header bg-white d-flex justify-content-between align-items-center">
                        <span class="fw-bold"><i class="bi bi-heart-fill text-success me-2"></i>Registered Volunteers (${volunteers.size()})</span>
                        <span class="badge bg-success">${stats.availableVolunteers} Ready for Deployment</span>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-custom table-hover align-middle">
                                <thead>
                                    <tr>
                                        <th>Volunteer</th>
                                        <th>Skills & Certifications</th>
                                        <th>Availability</th>
                                        <th>Assign Task</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="vol" items="${volunteers}">
                                        <tr>
                                            <td>
                                                <div class="fw-bold text-dark"><c:out value="${vol.fullName}" /></div>
                                                <div class="small text-muted"><i class="bi bi-telephone me-1"></i><c:out value="${vol.phone}" /></div>
                                            </td>
                                            <td>
                                                <div class="small text-dark fw-semibold"><c:out value="${vol.skills}" /></div>
                                                <div class="small text-muted"><c:out value="${vol.location}" /> &bull; <c:out value="${vol.experienceYears}" /> yr(s) exp</div>
                                            </td>
                                            <td><span class="badge ${vol.availability.badgeClass}"><c:out value="${vol.availability.displayName}" /></span></td>
                                            <td>
                                                <button type="button" class="btn btn-sm btn-outline-success" data-bs-toggle="modal" data-bs-target="#assignVolunteerModal${vol.id}">
                                                    <i class="bi bi-plus-circle me-1"></i>Assign
                                                </button>

                                                <!-- Assign Volunteer Modal -->
                                                <div class="modal fade" id="assignVolunteerModal${vol.id}" tabindex="-1">
                                                    <div class="modal-dialog">
                                                        <div class="modal-content">
                                                            <form action="${pageContext.request.contextPath}/admin/volunteers/assign" method="POST">
                                                                <input type="hidden" name="volunteerId" value="${vol.id}">
                                                                <div class="modal-header bg-success text-white">
                                                                    <h5 class="modal-title fs-6 fw-bold">Assign Incident Task to ${vol.fullName}</h5>
                                                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    <div class="mb-3">
                                                                        <label class="form-label">Task Title <span class="text-danger">*</span></label>
                                                                        <input type="text" name="taskTitle" class="form-control" placeholder="e.g. Shelter Medical Triage / Distribution" required>
                                                                    </div>
                                                                    <div class="mb-3">
                                                                        <label class="form-label">Task Description & Instructions</label>
                                                                        <textarea name="taskDescription" class="form-control" rows="3" placeholder="Provide operational instructions, meeting point, and supervisor details..."></textarea>
                                                                    </div>
                                                                    <div class="mb-3">
                                                                        <label class="form-label">Link to Disaster Report (Optional)</label>
                                                                        <select name="reportId" class="form-select">
                                                                            <option value="">-- No specific report linked --</option>
                                                                            <c:forEach var="rp" items="${reports}">
                                                                                <option value="${rp.id}">${rp.code}: ${rp.title}</option>
                                                                            </c:forEach>
                                                                        </select>
                                                                    </div>
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                                                                    <button type="submit" class="btn btn-success btn-sm fw-bold">Deploy Volunteer</button>
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
                    </div>
                </div>
            </div>

            <!-- Rescue Squads Overview -->
            <div class="col-lg-6">
                <div class="card card-theme h-100">
                    <div class="card-header bg-white d-flex justify-content-between align-items-center">
                        <span class="fw-bold"><i class="bi bi-shield-shaded text-primary me-2"></i>Rescue Squads Readiness (${rescueTeams.size()})</span>
                        <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#createRescueTeamModal">
                            <i class="bi bi-plus-lg me-1"></i>New Squad
                        </button>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-custom table-hover align-middle">
                                <thead>
                                    <tr>
                                        <th>Squad Name</th>
                                        <th>Specialization</th>
                                        <th>Members</th>
                                        <th>Operational Area</th>
                                        <th>Readiness</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="squad" items="${rescueTeams}">
                                        <tr>
                                            <td>
                                                <div class="fw-bold text-dark"><c:out value="${squad.teamName}" /></div>
                                                <div class="small text-muted"><i class="bi bi-telephone me-1"></i><c:out value="${squad.contactNumber}" /></div>
                                            </td>
                                            <td><c:out value="${squad.specialization}" /></td>
                                            <td><strong><c:out value="${squad.memberCount}" /></strong> crew</td>
                                            <td><c:out value="${squad.operationalArea}" /></td>
                                            <td><span class="badge ${squad.availability.badgeClass}"><c:out value="${squad.availability.displayName}" /></span></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Rescue Squad Modal -->
        <div class="modal fade" id="createRescueTeamModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/admin/rescue-teams/add" method="POST">
                        <div class="modal-header bg-primary text-white">
                            <h5 class="modal-title fs-6 fw-bold">Commission New Emergency Rescue Squad</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label class="form-label">Rescue Squad Name <span class="text-danger">*</span></label>
                                <input type="text" name="teamName" class="form-control" placeholder="e.g. Task Force Charlie - Hazmat" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Squad Specialization <span class="text-danger">*</span></label>
                                <input type="text" name="specialization" class="form-control" placeholder="e.g. Mountain / High Angle Extraction" required>
                            </div>
                            <div class="row g-3 mb-3">
                                <div class="col-6">
                                    <label class="form-label">Active Members Count</label>
                                    <input type="number" name="memberCount" class="form-control" value="6" min="1" max="100">
                                </div>
                                <div class="col-6">
                                    <label class="form-label">Emergency Comms Phone <span class="text-danger">*</span></label>
                                    <input type="tel" name="contactNumber" class="form-control" placeholder="+1-555-0199" required>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Operational Area / Sector</label>
                                <input type="text" name="operationalArea" class="form-control" placeholder="e.g. Western Mountain Corridor">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-primary btn-sm fw-bold">Commission Squad</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Recent Activity Audit Log -->
        <div class="card card-theme">
            <div class="card-header bg-white">
                <span class="fw-bold fs-6"><i class="bi bi-clock-history text-secondary me-2"></i>Real-Time Incident & Operation Event Log</span>
            </div>
            <div class="card-body p-0">
                <div class="list-group list-group-flush">
                    <c:forEach var="act" items="${recentActivities}">
                        <div class="list-group-item d-flex justify-content-between align-items-center p-3">
                            <div class="d-flex align-items-center">
                                <span class="badge ${act.type == 'REPORT' ? 'bg-primary' : 'bg-danger'} me-3 p-2">
                                    <i class="bi ${act.type == 'REPORT' ? 'bi-broadcast' : 'bi-life-preserver'}"></i>
                                </span>
                                <div>
                                    <div class="fw-bold text-dark"><c:out value="${act.title}" /></div>
                                    <div class="small text-muted">Tracking Reference: <span class="font-monospace fw-bold"><c:out value="${act.code}" /></span></div>
                                </div>
                            </div>
                            <div class="text-end">
                                <span class="badge bg-secondary mb-1"><c:out value="${act.severity}" /></span>
                                <div class="small text-muted"><c:out value="${act.timestamp}" /></div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

