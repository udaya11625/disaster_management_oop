<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Relief Resources & Inventory Management" />
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
                <h1 class="fw-bold text-dark mb-0"><i class="bi bi-boxes text-success me-2"></i>Relief Supplies & Resource Inventory</h1>
                <p class="text-muted small mb-0">Track warehouse stockpiles, restock supplies, enforce non-negative stock transactions, and audit distributions.</p>
            </div>
            <div class="d-flex gap-2">
                <button type="button" class="btn btn-success fw-bold shadow-sm" data-bs-toggle="modal" data-bs-target="#addResourceModal">
                    <i class="bi bi-plus-circle me-1"></i>Add New Resource Item
                </button>
            </div>
        </div>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <!-- Low Stock Warning Alert if any -->
        <c:if test="${not empty lowStock}">
            <div class="alert alert-warning border-warning shadow-sm d-flex align-items-center mb-4">
                <i class="bi bi-exclamation-triangle-fill fs-3 text-danger me-3"></i>
                <div>
                    <h6 class="fw-bold text-dark mb-1">Attention: Critical Stock Shortages Detected</h6>
                    <div class="small text-secondary">
                        <c:forEach var="ls" items="${lowStock}" varStatus="loop">
                            <strong><c:out value="${ls.name}" /></strong> (${ls.quantity} ${ls.unit} remaining; min threshold: ${ls.minStockLevel})${not loop.last ? ' &bull; ' : ''}
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Current Inventory Table -->
        <div class="card card-theme mb-4">
            <div class="card-header bg-white d-flex justify-content-between align-items-center">
                <span class="fw-bold fs-6"><i class="bi bi-box-seam text-primary me-2"></i>Warehouse Inventory Stockpile (${resources.size()} Categories)</span>
                <span class="small text-muted">Strict Transaction Locks Enabled</span>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-custom table-hover align-middle">
                        <thead>
                            <tr>
                                <th>Item Name</th>
                                <th>Category</th>
                                <th>Available Stock</th>
                                <th>Storage Location</th>
                                <th>Min Threshold</th>
                                <th>Stock Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="res" items="${resources}">
                                <tr class="${res.lowStock ? 'table-warning' : ''}">
                                    <td>
                                        <div class="fw-bold text-dark"><c:out value="${res.name}" /></div>
                                    </td>
                                    <td>
                                        <span class="badge ${res.category.badgeClass}">
                                            <i class="bi ${res.category.iconClass} me-1"></i><c:out value="${res.category.displayName}" />
                                        </span>
                                    </td>
                                    <td>
                                        <span class="fs-6 fw-bold ${res.lowStock ? 'text-danger' : 'text-success'}">
                                            <c:out value="${res.quantity}" />
                                        </span>
                                        <span class="small text-muted"><c:out value="${res.unit}" /></span>
                                    </td>
                                    <td><i class="bi bi-building me-1 text-muted"></i><c:out value="${res.storageLocation}" /></td>
                                    <td><c:out value="${res.minStockLevel}" /> <c:out value="${res.unit}" /></td>
                                    <td>${res.stockBadge}</td>
                                    <td>
                                        <div class="d-flex gap-1">
                                            <!-- Restock Modal Button -->
                                            <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#restockModal${res.id}" title="Restock Quantity">
                                                <i class="bi bi-box-arrow-in-down me-1"></i>Restock
                                            </button>
                                            <!-- Distribute Modal Button -->
                                            <button type="button" class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#distributeModal${res.id}" 
                                                    ${res.quantity <= 0 ? 'disabled' : ''} title="Dispatch / Distribute">
                                                <i class="bi bi-truck me-1"></i>Distribute
                                            </button>
                                        </div>

                                        <!-- Restock Modal -->
                                        <div class="modal fade" id="restockModal${res.id}" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form action="${pageContext.request.contextPath}/admin/resources/restock" method="POST">
                                                        <input type="hidden" name="resourceId" value="${res.id}">
                                                        <div class="modal-header bg-primary text-white">
                                                            <h5 class="modal-title fs-6 fw-bold">Restock: ${res.name}</h5>
                                                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <p class="small text-muted mb-3">Current warehouse inventory: <strong>${res.quantity} ${res.unit}</strong>.</p>
                                                            <div class="mb-3">
                                                                <label class="form-label">Units to Add <span class="text-danger">*</span></label>
                                                                <input type="number" name="addAmount" class="form-control" min="1" max="100000" placeholder="e.g. 500" required>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-primary btn-sm fw-bold">Add to Stock</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Distribute Modal -->
                                        <div class="modal fade" id="distributeModal${res.id}" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form action="${pageContext.request.contextPath}/admin/resources/distribute" method="POST">
                                                        <input type="hidden" name="resourceId" value="${res.id}">
                                                        <div class="modal-header bg-danger text-white">
                                                            <h5 class="modal-title fs-6 fw-bold">Distribute Supplies: ${res.name}</h5>
                                                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <div class="alert alert-light border small mb-3">
                                                                Available in Warehouse: <strong>${res.quantity} ${res.unit}</strong>.
                                                                <em>System validates against negative deductions.</em>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Quantity to Distribute <span class="text-danger">*</span></label>
                                                                <input type="number" name="quantity" class="form-control" min="1" max="${res.quantity}" required>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Destination Type <span class="text-danger">*</span></label>
                                                                <select name="destinationType" class="form-select" required>
                                                                    <option value="SHELTER">Designated Evacuation Shelter</option>
                                                                    <option value="RESCUE_MISSION">Field Rescue Operation Unit</option>
                                                                    <option value="DIRECT_COMMUNITY">Direct Community Drop-off Point</option>
                                                                </select>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Specific Destination / Shelter Name <span class="text-danger">*</span></label>
                                                                <input type="text" name="destinationName" class="form-control" placeholder="e.g. Central High School Shelter or River Sector 4" required>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Receiving Contact Person</label>
                                                                <input type="text" name="recipientContact" class="form-control" placeholder="Name and phone number of recipient">
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Audit Remarks / Notes</label>
                                                                <textarea name="remarks" class="form-control" rows="2" placeholder="Driver name, vehicle number, dispatch urgency..."></textarea>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-danger btn-sm fw-bold">Authorize Distribution</button>
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

        <!-- Add Resource Modal -->
        <div class="modal fade" id="addResourceModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/admin/resources/add" method="POST">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title fs-6 fw-bold">Add Relief Resource to Inventory</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label class="form-label">Resource Item Name <span class="text-danger">*</span></label>
                                <input type="text" name="name" class="form-control" placeholder="e.g. Purified Drinking Water (10L)" required>
                            </div>
                            <div class="row g-2 mb-3">
                                <div class="col-6">
                                    <label class="form-label">Category <span class="text-danger">*</span></label>
                                    <select name="category" class="form-select" required>
                                        <c:forEach var="cat" items="${categories}">
                                            <option value="${cat.name()}">${cat.displayName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-6">
                                    <label class="form-label">Unit of Measure <span class="text-danger">*</span></label>
                                    <input type="text" name="unit" class="form-control" placeholder="packs, liters, boxes, kits" required>
                                </div>
                            </div>
                            <div class="row g-2 mb-3">
                                <div class="col-6">
                                    <label class="form-label">Initial Quantity</label>
                                    <input type="number" name="quantity" class="form-control" value="100" min="0" required>
                                </div>
                                <div class="col-6">
                                    <label class="form-label">Minimum Stock Alert Threshold</label>
                                    <input type="number" name="minStockLevel" class="form-control" value="50" min="1" required>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Warehouse Storage Depot <span class="text-danger">*</span></label>
                                <input type="text" name="storageLocation" class="form-control" placeholder="e.g. Central Logistics Hub Sector 2" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                            <button type="submit" class="btn btn-success btn-sm fw-bold">Save New Resource</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Distribution Audit Trail Table -->
        <div class="card card-theme">
            <div class="card-header bg-white">
                <span class="fw-bold fs-6"><i class="bi bi-clock-history text-secondary me-2"></i>Resource Distribution Audit Trail (${distributions.size()} logged transactions)</span>
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${not empty distributions}">
                        <div class="table-responsive">
                            <table class="table table-custom table-hover align-middle">
                                <thead>
                                    <tr>
                                        <th>Date & Time</th>
                                        <th>Resource Item</th>
                                        <th>Quantity Dispatched</th>
                                        <th>Destination</th>
                                        <th>Recipient Contact</th>
                                        <th>Authorized By</th>
                                        <th>Remarks</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="dist" items="${distributions}">
                                        <tr>
                                            <td><small class="text-dark fw-semibold"><c:out value="${dist.distributionDate}" /></small></td>
                                            <td><strong><c:out value="${dist.resourceName}" /></strong></td>
                                            <td><span class="badge bg-danger fs-6"><c:out value="${dist.quantity}" /> <c:out value="${dist.resourceUnit}" /></span></td>
                                            <td>
                                                <span class="badge bg-secondary mb-1"><c:out value="${dist.destinationType}" /></span>
                                                <div class="small fw-semibold text-dark"><c:out value="${dist.destinationName}" /></div>
                                            </td>
                                            <td><small class="text-muted"><c:out value="${not empty dist.recipientContact ? dist.recipientContact : 'N/A'}" /></small></td>
                                            <td><small class="text-primary fw-semibold"><c:out value="${not empty dist.distributedByName ? dist.distributedByName : 'Admin'}" /></small></td>
                                            <td><small class="text-secondary"><c:out value="${not empty dist.remarks ? dist.remarks : '-'}" /></small></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-4 text-muted">No distribution transactions logged yet.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

