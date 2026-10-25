<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Find Emergency Relief Shelters" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <!-- Title and Quick Actions -->
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-4">
            <div>
                <h1 class="fw-bold text-dark mb-1"><i class="bi bi-building-check text-primary me-2"></i>Designated Safe Shelters</h1>
                <p class="text-muted mb-0">Live directory of active disaster relief evacuation centers, current occupancies, and medical support.</p>
            </div>
            <c:if test="${sessionScope.currentUser != null && sessionScope.currentUser.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/admin/shelters" class="btn btn-warning text-dark fw-bold">
                    <i class="bi bi-gear-fill me-1"></i>Manage Shelters & Allocations
                </a>
            </c:if>
        </div>

        <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

        <!-- Filter Bar -->
        <div class="card card-theme mb-4">
            <div class="card-body p-4">
                <form action="${pageContext.request.contextPath}/shelters" method="GET" class="row g-3 align-items-end">
                    <div class="col-md-5">
                        <label class="form-label small">Search Shelters</label>
                        <div class="input-group">
                            <span class="input-group-text bg-light"><i class="bi bi-search"></i></span>
                            <input type="text" name="keyword" class="form-control" placeholder="Search by shelter name or address..." value="<c:out value='${pKeyword}' />">
                        </div>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small">City / Municipality</label>
                        <input type="text" name="city" class="form-control" placeholder="City name..." value="<c:out value='${pCity}' />">
                    </div>
                    <div class="col-md-2">
                        <div class="form-check mb-2">
                            <input class="form-check-input" type="checkbox" name="medicalOnly" value="true" id="medicalCheck" ${pMedicalOnly ? 'checked' : ''}>
                            <label class="form-check-label small fw-semibold" for="medicalCheck">
                                <i class="bi bi-heart-pulse-fill text-danger me-1"></i>Medical Aid On-Site
                            </label>
                        </div>
                    </div>
                    <div class="col-md-2 d-flex gap-2">
                        <button type="submit" class="btn btn-primary w-100 fw-semibold"><i class="bi bi-search me-1"></i>Find</button>
                        <a href="${pageContext.request.contextPath}/shelters" class="btn btn-outline-secondary" title="Clear Filters"><i class="bi bi-arrow-counterclockwise"></i></a>
                    </div>
                </form>
            </div>
        </div>

        <!-- Shelters Grid -->
        <div class="row g-4">
            <c:choose>
                <c:when test="${not empty shelters}">
                    <c:forEach var="shelter" items="${shelters}">
                        <div class="col-md-6 col-lg-4">
                            <div class="card card-theme h-100 shadow-sm border">
                                <div class="card-body d-flex flex-column">
                                    <div class="d-flex justify-content-between align-items-start mb-2">
                                        <span class="badge ${shelter.status.badgeClass}"><c:out value="${shelter.status.displayName}" /></span>
                                        <span class="text-muted small"><i class="bi bi-geo-alt-fill text-danger me-1"></i><c:out value="${shelter.city}" /></span>
                                    </div>
                                    <h5 class="fw-bold text-dark mb-2"><c:out value="${shelter.name}" /></h5>
                                    <p class="text-secondary small mb-3"><c:out value="${shelter.address}" /></p>

                                    <!-- Capacity Progress Bar -->
                                    <div class="mb-3 p-3 bg-light rounded-3">
                                        <div class="d-flex justify-content-between align-items-center small mb-1">
                                            <span>Occupancy Rate</span>
                                            <span class="fw-bold"><c:out value="${shelter.currentOccupancy}" /> / <c:out value="${shelter.totalCapacity}" /></span>
                                        </div>
                                        <div class="progress" style="height: 8px;">
                                            <div class="progress-bar ${shelter.occupancyRate >= 100 ? 'bg-danger' : shelter.occupancyRate > 70 ? 'bg-warning' : 'bg-success'}" 
                                                 role="progressbar" style="width: ${shelter.occupancyRate}%;"></div>
                                        </div>
                                        <div class="d-flex justify-content-between align-items-center small mt-2">
                                            <span class="${shelter.availableCapacity > 0 ? 'text-success' : 'text-danger'} fw-bold">
                                                <i class="bi bi-door-open me-1"></i><c:out value="${shelter.availableCapacity}" /> spots remaining
                                            </span>
                                            <span class="text-muted">${Math.round(shelter.occupancyRate)}% full</span>
                                        </div>
                                    </div>

                                    <!-- Available Facilities -->
                                    <div class="mb-3">
                                        <div class="text-muted small fw-bold mb-2">Available On-Site Amenities:</div>
                                        <div class="d-flex flex-wrap gap-1">
                                            <c:if test="${shelter.hasFood}">
                                                <span class="badge bg-secondary bg-opacity-25 text-dark"><i class="bi bi-cup-hot text-primary me-1"></i>Food</span>
                                            </c:if>
                                            <c:if test="${shelter.hasWater}">
                                                <span class="badge bg-secondary bg-opacity-25 text-dark"><i class="bi bi-droplet text-info me-1"></i>Clean Water</span>
                                            </c:if>
                                            <c:if test="${shelter.hasMedical}">
                                                <span class="badge bg-danger bg-opacity-10 text-danger fw-bold"><i class="bi bi-heart-pulse-fill me-1"></i>Medical Clinic</span>
                                            </c:if>
                                            <c:if test="${shelter.hasSanitation}">
                                                <span class="badge bg-secondary bg-opacity-25 text-dark"><i class="bi bi-shield-check text-success me-1"></i>Sanitation</span>
                                            </c:if>
                                            <c:if test="${shelter.hasElectricity}">
                                                <span class="badge bg-secondary bg-opacity-25 text-dark"><i class="bi bi-lightning text-warning me-1"></i>Power</span>
                                            </c:if>
                                            <c:if test="${shelter.hasBedding}">
                                                <span class="badge bg-secondary bg-opacity-25 text-dark"><i class="bi bi-layers text-secondary me-1"></i>Beds/Cots</span>
                                            </c:if>
                                        </div>
                                    </div>

                                    <!-- Contact Footer -->
                                    <div class="mt-auto pt-3 border-top text-muted small">
                                        <div><i class="bi bi-person me-1"></i>Coordinator: <strong><c:out value="${shelter.contactPerson}" /></strong></div>
                                        <div><i class="bi bi-telephone me-1"></i>Phone: <strong><c:out value="${shelter.contactPhone}" /></strong></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12 text-center py-5">
                        <i class="bi bi-building-x text-muted fs-1 mb-3 d-block"></i>
                        <h5 class="text-muted">No shelters matched your search criteria.</h5>
                        <a href="${pageContext.request.contextPath}/shelters" class="btn btn-outline-primary btn-sm mt-2">View All Shelters</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

