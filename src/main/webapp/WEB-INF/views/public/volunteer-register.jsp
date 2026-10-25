<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Volunteer Registration - Disaster Response Corps" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <!-- Header -->
                <div class="text-center mb-4">
                    <span class="badge bg-success text-uppercase px-3 py-2 mb-2"><i class="bi bi-heart-fill me-1"></i>Community Hero Network</span>
                    <h1 class="fw-bold text-dark">Register as a Disaster Response Volunteer</h1>
                    <p class="text-muted">Join local emergency teams to provide relief aid, medical first assistance, and community support during catastrophic disasters.</p>
                </div>

                <%@ include file="/WEB-INF/views/common/alerts.jsp" %>

                <!-- Volunteer Registration Card -->
                <div class="card card-theme shadow-sm">
                    <div class="card-body p-4 p-md-5">
                        <form action="${pageContext.request.contextPath}/volunteer-register" method="POST" class="needs-validation" novalidate>
                            
                            <h5 class="fw-bold text-dark border-bottom pb-2 mb-3">1. Account Credentials & Contact</h5>
                            <div class="row g-3 mb-4">
                                <div class="col-md-6">
                                    <label class="form-label">Full Name <span class="text-danger">*</span></label>
                                    <input type="text" name="fullName" class="form-control" placeholder="Jane Doe" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Choose Username <span class="text-danger">*</span></label>
                                    <input type="text" name="username" class="form-control" placeholder="jane_volunteer" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Email Address <span class="text-danger">*</span></label>
                                    <input type="email" name="email" class="form-control" placeholder="jane@email.com" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Create Password <span class="text-danger">*</span></label>
                                    <input type="password" name="password" class="form-control" placeholder="At least 6 characters" minlength="6" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Primary Mobile Phone <span class="text-danger">*</span></label>
                                    <input type="tel" name="phone" class="form-control" placeholder="+1-555-0199" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Residential District / Location <span class="text-danger">*</span></label>
                                    <input type="text" name="location" class="form-control" placeholder="North District / Central Sector" required>
                                </div>
                                <div class="col-md-12">
                                    <label class="form-label">Street Address</label>
                                    <input type="text" name="address" class="form-control" placeholder="Street, apartment or postal area">
                                </div>
                            </div>

                            <h5 class="fw-bold text-dark border-bottom pb-2 mb-3">2. Capabilities & Disaster Experience</h5>
                            <div class="row g-3 mb-4">
                                <div class="col-md-12">
                                    <label class="form-label">Specialized Skills & Certifications <span class="text-danger">*</span></label>
                                    <textarea name="skills" class="form-control" rows="3" placeholder="e.g. Certified CPR/EMT, Drone Operator, Search & Rescue, Commercial Driver, Firefighting, Counseling, Logistics Management" required></textarea>
                                    <div class="form-text">Include any professional training, medical licenses, or emergency experience.</div>
                                </div>
                                <div class="col-md-8">
                                    <label class="form-label">Preferred Incident Assignments</label>
                                    <select name="preferredAssignments" class="form-select">
                                        <option value="First Aid Station & Triage">First Aid Station & Medical Triage</option>
                                        <option value="Evacuation & Transport Assistance">Evacuation & Transport Assistance</option>
                                        <option value="Food & Relief Ration Logistics">Food & Relief Ration Logistics</option>
                                        <option value="Emergency Shelter Coordinator">Emergency Shelter Coordinator</option>
                                        <option value="Search & Reconnaissance">Search & Reconnaissance</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Years of Experience</label>
                                    <input type="number" name="experienceYears" class="form-control" min="0" max="40" value="1">
                                </div>
                            </div>

                            <!-- Submit Button -->
                            <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                                <a href="${pageContext.request.contextPath}/login" class="text-muted small">Already registered? Log in</a>
                                <button type="submit" class="btn btn-success btn-lg px-4 fw-bold">
                                    <i class="bi bi-heart-fill me-2"></i>Complete Volunteer Enrollment
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

