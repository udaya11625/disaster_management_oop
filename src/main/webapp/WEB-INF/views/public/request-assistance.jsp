<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Request Emergency Assistance (SOS)" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <!-- SOS Header -->
                <div class="p-4 rounded-4 bg-danger text-white shadow-sm mb-4">
                    <div class="d-flex align-items-center mb-2">
                        <span class="badge bg-white text-danger fw-bold text-uppercase me-2"><i class="bi bi-shield-fill-plus me-1"></i>Priority Emergency Dispatch</span>
                        <span class="small opacity-75">SOS Assistance Portal</span>
                    </div>
                    <h2 class="fw-bold mb-1">Request Emergency Rescue Assistance</h2>
                    <p class="mb-0 text-white-50 small">
                        For citizens trapped, injured, or in need of urgent evacuation and medical supplies during an active disaster.
                    </p>
                </div>

                <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

                <!-- Assistance Request Form -->
                <div class="card card-theme shadow-sm">
                    <div class="card-body p-4 p-md-5">
                        <form action="${pageContext.request.contextPath}/request-assistance" method="POST" class="needs-validation" novalidate>
                            
                            <h5 class="fw-bold text-dark border-bottom pb-2 mb-3">1. Contact & Identity Information</h5>
                            <div class="row g-3 mb-4">
                                <div class="col-md-6">
                                    <label class="form-label">Full Name of Contact Person <span class="text-danger">*</span></label>
                                    <input type="text" name="contactName" class="form-control" placeholder="Contact person's name" 
                                           value="<c:out value='${defaultName}' />" required>
                                    <div class="invalid-feedback">Contact name is required.</div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Primary Mobile / Callback Phone <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="bi bi-telephone"></i></span>
                                        <input type="tel" name="contactPhone" class="form-control" placeholder="+1-555-0100" 
                                               value="<c:out value='${defaultPhone}' />" required>
                                    </div>
                                    <div class="invalid-feedback">Valid contact number is required.</div>
                                </div>
                                <div class="col-md-12">
                                    <label class="form-label">Email Address (Optional)</label>
                                    <input type="email" name="contactEmail" class="form-control" placeholder="name@email.com" 
                                           value="<c:out value='${defaultEmail}' />">
                                </div>
                            </div>

                            <h5 class="fw-bold text-dark border-bottom pb-2 mb-3">2. Disaster & Location Details</h5>
                            <div class="row g-3 mb-4">
                                <div class="col-md-6">
                                    <label class="form-label">Disaster Hazard Occurring <span class="text-danger">*</span></label>
                                    <select name="disasterType" class="form-select" required>
                                        <c:forEach var="t" items="${disasterTypes}">
                                            <option value="${t.name()}">${t.displayName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Number of People Affected / Trapped <span class="text-danger">*</span></label>
                                    <input type="number" name="peopleAffected" class="form-control" min="1" max="5000" value="1" required>
                                </div>
                                <div class="col-md-8">
                                    <label class="form-label">Exact Location / Address Where Assistance is Needed <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="bi bi-geo-alt-fill text-danger"></i></span>
                                        <input type="text" name="location" class="form-control" placeholder="House number, street, flood sector" 
                                               value="<c:out value='${defaultAddress}' />" required>
                                    </div>
                                    <div class="invalid-feedback">Rescue location is required.</div>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Nearest Landmark</label>
                                    <input type="text" name="landmark" class="form-control" placeholder="Visible landmark">
                                </div>
                            </div>

                            <h5 class="fw-bold text-dark border-bottom pb-2 mb-3">3. Type of Assistance & Urgency</h5>
                            <div class="row g-3 mb-4">
                                <div class="col-md-6">
                                    <label class="form-label">Primary Assistance Requested <span class="text-danger">*</span></label>
                                    <select name="assistanceType" class="form-select" required>
                                        <option value="EVACUATION">Evacuation / Stranded & Water Rising</option>
                                        <option value="MEDICAL_AID">Urgent Medical Attention / Trauma</option>
                                        <option value="SEARCH_RESCUE">Search & Rescue / Trapped under debris</option>
                                        <option value="FOOD_WATER">Emergency Food & Drinking Water</option>
                                        <option value="TEMPORARY_SHELTER">Temporary Shelter Relocation</option>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Emergency Priority Level <span class="text-danger">*</span></label>
                                    <select name="priority" class="form-select" required>
                                        <option value="CRITICAL" class="text-danger fw-bold">CRITICAL - Immediate Threat to Life</option>
                                        <option value="HIGH" selected>HIGH - Urgent response required</option>
                                        <option value="MEDIUM">MEDIUM - Moderate, stable for now</option>
                                        <option value="LOW">LOW - Non-critical aid</option>
                                    </select>
                                </div>
                                <div class="col-md-12">
                                    <label class="form-label">Special Needs, Medical Conditions, or Notes</label>
                                    <textarea name="specialNeeds" class="form-control" rows="3" placeholder="Specify if there are elderly persons, infants, disabled individuals needing wheelchairs, prescription oxygen, or injuries..."></textarea>
                                </div>
                            </div>

                            <!-- Submit Action -->
                            <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                                <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary">Cancel</a>
                                <button type="submit" class="btn btn-danger btn-lg px-4 fw-bold">
                                    <i class="bi bi-broadcast me-2"></i>Dispatch Emergency Assistance Request
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Emergency Dispatch Hotline Notice -->
                <div class="p-3 mt-4 rounded-3 bg-dark text-white d-flex align-items-center justify-content-between flex-wrap gap-2">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-telephone-fill text-danger fs-3 me-3"></i>
                        <div>
                            <div class="fw-bold">24-Hour Voice Hotline Dispatch</div>
                            <small class="text-white-50">If internet fails, call 1-800-555-DISASTER for immediate telephone dispatch.</small>
                        </div>
                    </div>
                    <span class="badge bg-danger fs-6 px-3 py-2">Toll-Free</span>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

