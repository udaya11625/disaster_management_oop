<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Report a Disaster Incident" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <!-- Header -->
                <div class="mb-4">
                    <a href="${pageContext.request.contextPath}/reports" class="text-decoration-none small text-muted mb-2 d-inline-block">
                        <i class="bi bi-arrow-left me-1"></i>Back to Reports Feed
                    </a>
                    <h1 class="fw-bold text-dark"><i class="bi bi-megaphone-fill text-danger me-2"></i>Report a Disaster or Hazard</h1>
                    <p class="text-muted">Provide accurate and timely details to aid emergency responders and community awareness.</p>
                </div>

                <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

                <!-- Report Form Card -->
                <div class="card card-theme shadow-sm">
                    <div class="card-body p-4 p-md-5">
                        <form action="${pageContext.request.contextPath}/report-disaster" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
                            
                            <!-- Disaster Type and Severity -->
                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">Disaster Category <span class="text-danger">*</span></label>
                                    <select name="disasterType" class="form-select" required>
                                        <option value="" disabled selected>Select emergency category...</option>
                                        <c:forEach var="t" items="${disasterTypes}">
                                            <option value="${t.name()}">${t.displayName}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="invalid-feedback">Please choose a disaster category.</div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Severity Level <span class="text-danger">*</span></label>
                                    <select name="severity" class="form-select" required>
                                        <c:forEach var="s" items="${severities}">
                                            <option value="${s.name()}" ${s.name() == 'HIGH' ? 'selected' : ''}>${s.displayName} - ${s.description}</option>
                                        </c:forEach>
                                    </select>
                                    <div class="invalid-feedback">Please select severity level.</div>
                                </div>
                            </div>

                            <!-- Incident Title -->
                            <div class="mb-3">
                                <label class="form-label">Incident Summary Title <span class="text-danger">*</span></label>
                                <input type="text" name="title" class="form-control" placeholder="e.g. Flash Flooding along Bridge Road, River breached" required maxlength="180">
                                <div class="invalid-feedback">Please provide a descriptive title.</div>
                            </div>

                            <!-- Location and Landmark -->
                            <div class="row g-3 mb-3">
                                <div class="col-md-7">
                                    <label class="form-label">Specific Location / Street Address <span class="text-danger">*</span></label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="bi bi-geo-alt"></i></span>
                                        <input type="text" name="location" class="form-control" placeholder="Street, Sector, or Highway Mile marker" required maxlength="255">
                                    </div>
                                    <div class="invalid-feedback">Location is required.</div>
                                </div>
                                <div class="col-md-5">
                                    <label class="form-label">Nearest Landmark</label>
                                    <input type="text" name="landmark" class="form-control" placeholder="e.g. Opposite City Hospital" maxlength="255">
                                </div>
                            </div>

                            <!-- Description -->
                            <div class="mb-3">
                                <label class="form-label">Incident Description & Immediate Hazard <span class="text-danger">*</span></label>
                                <textarea name="description" class="form-control" rows="4" placeholder="Detail the situation: extent of water depth, collapsed structure, downed power lines, stranded individuals..." required></textarea>
                                <div class="invalid-feedback">Please describe the emergency incident.</div>
                            </div>

                            <!-- Evidence: File Upload or Image URL -->
                            <div class="row g-3 mb-4">
                                <div class="col-md-6">
                                    <label class="form-label">Upload Photo / Evidence (Optional)</label>
                                    <input type="file" name="imageFile" class="form-control" accept="image/*">
                                    <div class="form-text">Supported formats: JPG, PNG, WEBP (Max: 10MB)</div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Or Image Web URL</label>
                                    <input type="url" name="imageUrl" class="form-control" placeholder="https://example.com/photo.jpg">
                                    <div class="form-text">Direct image web address</div>
                                </div>
                            </div>

                            <!-- Submit Action -->
                            <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                                <span class="text-muted small"><i class="bi bi-lock me-1"></i>Saved securely to centralized database</span>
                                <button type="submit" class="btn btn-danger btn-lg px-4 fw-bold">
                                    <i class="bi bi-send-fill me-2"></i>Submit Official Disaster Report
                                </button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Emergency Notice Box -->
                <div class="card bg-light border-0 mt-4 p-3 rounded-3">
                    <div class="d-flex align-items-center">
                        <i class="bi bi-info-circle-fill text-primary fs-3 me-3"></i>
                        <div class="small text-muted">
                            <strong>Note on False Reports:</strong> Misleading or fraudulent disaster reporting impedes emergency operations. All reports are permanently audited and tracked with reference IDs.
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
