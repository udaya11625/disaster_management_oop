<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Emergency Shelter Administration" />
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
                <h1 class="fw-bold text-dark mb-0"><i class="bi bi-building-gear text-primary me-2"></i>Shelter Logistics & Evacuee Allocation</h1>
                <p class="text-muted small mb-0">Monitor real-time capacity, execute safe evacuee allocations, and manage emergency shelter facilities.</p>
            </div>
            <div class="d-flex gap-2">
                <button type="button" class="btn btn-primary fw-bold shadow-sm" data-bs-toggle="modal" data-bs-target="#addShelterModal">
                    <i class="bi bi-plus-circle me-1"></i>Add New Shelter
                </button>
            </div>
        </div>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <!-- Shelters Table Card -->
        <div class="card card-theme mb-4">
            <div class="card-header bg-white d-flex justify-content-between align-items-center">
                <span class="fw-bold fs-6"><i class="bi bi-building-check text-success me-2"></i>Designated Shelter Network (${shelters.size()} Facilities)</span>
                <span class="small text-muted">Transactional Capacity Enforcement Active</span>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-custom table-hover align-middle">
                        <thead>
                            <tr>
                                <th>Shelter Name & City</th>
                                <th>Address & Contact</th>
                                <th>Total Capacity</th>
                                <th>Current Occupancy</th>
                                <th>Available Spots</th>
                                <th>Occupancy Bar</th>
                                <th>Facilities</th>
                                <th>Status</th>
                                <th>Operations</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="shelter" items="${shelters}">
                                <tr>
                                    <td>
                                        <div class="fw-bold text-dark"><c:out value="${shelter.name}" /></div>
                                        <div class="small text-muted"><i class="bi bi-geo-alt-fill text-danger me-1"></i><c:out value="${shelter.city}" /></div>
                                    </td>
                                    <td>
                                        <div class="small text-secondary"><c:out value="${shelter.address}" /></div>
                                        <div class="small text-muted"><i class="bi bi-telephone me-1"></i><c:out value="${shelter.contactPhone}" /> (<c:out value="${shelter.contactPerson}" />)</div>
                                    </td>
                                    <td><strong><c:out value="${shelter.totalCapacity}" /></strong></td>
                                    <td><strong><c:out value="${shelter.currentOccupancy}" /></strong></td>
                                    <td>
                                        <span class="badge ${shelter.availableCapacity > 0 ? 'bg-success' : 'bg-danger'}">
                                            <c:out value="${shelter.availableCapacity}" /> free
                                        </span>
                                    </td>
                                    <td style="min-width: 140px;">
                                        <div class="progress" style="height: 10px;">
                                            <div class="progress-bar ${shelter.occupancyRate >= 100 ? 'bg-danger' : shelter.occupancyRate > 70 ? 'bg-warning' : 'bg-success'}" 
                                                 role="progressbar" style="width: ${shelter.occupancyRate}%;"></div>
                                        </div>
                                        <small class="text-muted">${Math.round(shelter.occupancyRate)}% full</small>
                                    </td>
                                    <td>
                                        <div class="d-flex gap-1 flex-wrap">
                                            <c:if test="${shelter.hasFood}"><span class="badge bg-secondary" title="Food Rations">F</span></c:if>
                                            <c:if test="${shelter.hasWater}"><span class="badge bg-info text-dark" title="Clean Water">W</span></c:if>
                                            <c:if test="${shelter.hasMedical}"><span class="badge bg-danger" title="Medical Clinic">M</span></c:if>
                                            <c:if test="${shelter.hasSanitation}"><span class="badge bg-success" title="Sanitation">S</span></c:if>
                                            <c:if test="${shelter.hasElectricity}"><span class="badge bg-warning text-dark" title="Generator Power">P</span></c:if>
                                        </div>
                                    </td>
                                    <td><span class="badge ${shelter.status.badgeClass}"><c:out value="${shelter.status.displayName}" /></span></td>
                                    <td>
                                        <div class="d-flex gap-1">
                                            <!-- Check-in Evacuees Button -->
                                            <button type="button" class="btn btn-sm btn-success" data-bs-toggle="modal" data-bs-target="#checkInModal${shelter.id}" 
                                                    ${shelter.availableCapacity <= 0 || shelter.status == 'INACTIVE' ? 'disabled' : ''} title="Allocate Evacuees">
                                                <i class="bi bi-person-plus-fill me-1"></i>Check-In
                                            </button>
                                            <!-- Edit Shelter Button -->
                                            <button type="button" class="btn btn-sm btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#editShelterModal${shelter.id}" title="Edit Shelter Details">
                                                <i class="bi bi-pencil"></i>
                                            </button>
                                        </div>

                                        <!-- Check-In Evacuees Modal -->
                                        <div class="modal fade" id="checkInModal${shelter.id}" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form action="${pageContext.request.contextPath}/admin/shelters/check-in" method="POST">
                                                        <input type="hidden" name="shelterId" value="${shelter.id}">
                                                        <div class="modal-header bg-success text-white">
                                                            <h5 class="modal-title fs-6 fw-bold">Check-In Evacuees: ${shelter.name}</h5>
                                                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <div class="alert alert-info py-2 small mb-3">
                                                                Available Capacity: <strong>${shelter.availableCapacity}</strong> spots remaining.
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Head of Household / Representative <span class="text-danger">*</span></label>
                                                                <input type="text" name="repName" class="form-control" placeholder="Representative Full Name" required>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Contact Phone <span class="text-danger">*</span></label>
                                                                <input type="tel" name="phone" class="form-control" placeholder="+1-555-0100" required>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Number of People in Group <span class="text-danger">*</span></label>
                                                                <input type="number" name="peopleCount" class="form-control" min="1" max="${shelter.availableCapacity}" value="1" required>
                                                                <div class="form-text">System will enforce transactional validation to prevent over-allocation.</div>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Notes / Medical Triage Details</label>
                                                                <textarea name="notes" class="form-control" rows="2" placeholder="Dietary restrictions, injuries, children..."></textarea>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-success btn-sm fw-bold">Confirm Allocation</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Edit Shelter Modal -->
                                        <div class="modal fade" id="editShelterModal${shelter.id}" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form action="${pageContext.request.contextPath}/admin/shelters/update" method="POST">
                                                        <input type="hidden" name="id" value="${shelter.id}">
                                                        <div class="modal-header bg-dark text-white">
                                                            <h5 class="modal-title fs-6 fw-bold">Edit Shelter Information</h5>
                                                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <div class="mb-3">
                                                                <label class="form-label">Shelter Name</label>
                                                                <input type="text" name="name" class="form-control" value="<c:out value='${shelter.name}' />" required>
                                                            </div>
                                                            <div class="row g-2 mb-3">
                                                                <div class="col-8">
                                                                    <label class="form-label">Address</label>
                                                                    <input type="text" name="address" class="form-control" value="<c:out value='${shelter.address}' />" required>
                                                                </div>
                                                                <div class="col-4">
                                                                    <label class="form-label">City</label>
                                                                    <input type="text" name="city" class="form-control" value="<c:out value='${shelter.city}' />" required>
                                                                </div>
                                                            </div>
                                                            <div class="row g-2 mb-3">
                                                                <div class="col-6">
                                                                    <label class="form-label">Contact Person</label>
                                                                    <input type="text" name="contactPerson" class="form-control" value="<c:out value='${shelter.contactPerson}' />" required>
                                                                </div>
                                                                <div class="col-6">
                                                                    <label class="form-label">Contact Phone</label>
                                                                    <input type="tel" name="contactPhone" class="form-control" value="<c:out value='${shelter.contactPhone}' />" required>
                                                                </div>
                                                            </div>
                                                            <div class="row g-2 mb-3">
                                                                <div class="col-6">
                                                                    <label class="form-label">Total Capacity</label>
                                                                    <input type="number" name="totalCapacity" class="form-control" value="${shelter.totalCapacity}" min="1" required>
                                                                </div>
                                                                <div class="col-6">
                                                                    <label class="form-label">Operational Status</label>
                                                                    <select name="status" class="form-select">
                                                                        <option value="ACTIVE" ${shelter.status == 'ACTIVE' ? 'selected' : ''}>Operational</option>
                                                                        <option value="FULL" ${shelter.status == 'FULL' ? 'selected' : ''}>At Capacity</option>
                                                                        <option value="INACTIVE" ${shelter.status == 'INACTIVE' ? 'selected' : ''}>Closed / Inactive</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label d-block">Available Facilities</label>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="checkbox" name="hasFood" id="eFood${shelter.id}" ${shelter.hasFood ? 'checked' : ''}>
                                                                    <label class="form-check-label small" for="eFood${shelter.id}">Food</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="checkbox" name="hasWater" id="eWater${shelter.id}" ${shelter.hasWater ? 'checked' : ''}>
                                                                    <label class="form-check-label small" for="eWater${shelter.id}">Water</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="checkbox" name="hasMedical" id="eMed${shelter.id}" ${shelter.hasMedical ? 'checked' : ''}>
                                                                    <label class="form-check-label small" for="eMed${shelter.id}">Medical</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="checkbox" name="hasSanitation" id="eSan${shelter.id}" ${shelter.hasSanitation ? 'checked' : ''}>
                                                                    <label class="form-check-label small" for="eSan${shelter.id}">Sanitation</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="checkbox" name="hasElectricity" id="eElec${shelter.id}" ${shelter.hasElectricity ? 'checked' : ''}>
                                                                    <label class="form-check-label small" for="eElec${shelter.id}">Power</label>
                                                                </div>
                                                                <div class="form-check form-check-inline">
                                                                    <input class="form-check-input" type="checkbox" name="hasBedding" id="eBed${shelter.id}" ${shelter.hasBedding ? 'checked' : ''}>
                                                                    <label class="form-check-label small" for="eBed${shelter.id}">Bedding</label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-primary btn-sm fw-bold">Update Shelter</button>
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

        <!-- Add Shelter Modal -->
        <div class="modal fade" id="addShelterModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/admin/shelters/add" method="POST">
                        <div class="modal-header bg-primary text-white">
                            <h5 class="modal-title fs-6 fw-bold">Add Designated Safe Shelter</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label class="form-label">Shelter Facility Name <span class="text-danger">*</span></label>
                                <input type="text" name="name" class="form-control" placeholder="e.g. Westside High School Shelter" required>
                            </div>
                            <div class="row g-2 mb-3">
                                <div class="col-8">
                                    <label class="form-label">Street Address <span class="text-danger">*</span></label>
                                    <input type="text" name="address" class="form-control" placeholder="Address" required>
                                </div>
                                <div class="col-4">
                                    <label class="form-label">City / District <span class="text-danger">*</span></label>
                                    <input type="text" name="city" class="form-control" placeholder="City" required>
                                </div>
                            </div>
                            <div class="row g-2 mb-3">
                                <div class="col-6">
                                    <label class="form-label">Contact Person <span class="text-danger">*</span></label>
                                    <input type="text" name="contactPerson" class="form-control" placeholder="Coordinator Name" required>
                                </div>
                                <div class="col-6">
                                    <label class="form-label">Contact Phone <span class="text-danger">*</span></label>
                                    <input type="tel" name="contactPhone" class="form-control" placeholder="+1-555-0100" required>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Total Evacuee Capacity <span class="text-danger">*</span></label>
                                <input type="number" name="totalCapacity" class="form-control" value="200" min="10" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label d-block">Facilities Available</label>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="hasFood" id="aFood" checked>
                                    <label class="form-check-label small" for="aFood">Food Rations</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="hasWater" id="aWater" checked>
                                    <label class="form-check-label small" for="aWater">Drinking Water</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="hasMedical" id="aMed">
                                    <label class="form-check-label small" for="aMed">Medical Station</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="hasSanitation" id="aSan" checked>
                                    <label class="form-check-label small" for="aSan">Sanitation</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="hasElectricity" id="aElec" checked>
                                    <label class="form-check-label small" for="aElec">Electricity/Gen</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="checkbox" name="hasBedding" id="aBed" checked>
                                    <label class="form-check-label small" for="aBed">Bedding/Cots</label>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-primary btn-sm fw-bold">Save & Activate Shelter</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

