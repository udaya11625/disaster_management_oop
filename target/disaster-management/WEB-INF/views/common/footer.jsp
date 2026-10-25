<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<footer class="footer-main">
    <div class="container">
        <div class="row g-4">
            <div class="col-lg-4 col-md-6">
                <div class="d-flex align-items-center mb-3">
                    <span class="brand-icon me-2"><i class="bi bi-shield-fill-exclamation"></i></span>
                    <span class="fs-5 fw-bold text-white">DisasterRescue<span class="text-danger">.GOV</span></span>
                </div>
                <p class="text-secondary small mb-3">
                    Centralized emergency response and disaster management platform built using Object-Oriented Java, Servlets, Tomcat 10.1, and MySQL.
                </p>
                <div class="p-3 rounded bg-dark border border-secondary border-opacity-25 mb-2">
                    <div class="text-danger fw-bold small text-uppercase mb-1"><i class="bi bi-telephone-inbound-fill me-1"></i>Emergency Dispatch Hotlines</div>
                    <div class="text-white fw-bold fs-6">Disaster Control: 1-800-555-DISASTER</div>
                    <div class="text-muted small">Medical & Ambulance: 911 / 112 &bull; Flood Helpline: 1-800-FLOOD-HELP</div>
                </div>
            </div>

            <div class="col-lg-2 col-md-6">
                <h6 class="footer-heading">Quick Actions</h6>
                <ul class="footer-links">
                    <li><a href="${pageContext.request.contextPath}/report-disaster"><i class="bi bi-chevron-right me-1"></i>Report Incident</a></li>
                    <li><a href="${pageContext.request.contextPath}/request-assistance"><i class="bi bi-chevron-right me-1"></i>Request SOS Aid</a></li>
                    <li><a href="${pageContext.request.contextPath}/shelters"><i class="bi bi-chevron-right me-1"></i>Find Shelters</a></li>
                    <li><a href="${pageContext.request.contextPath}/volunteer-register"><i class="bi bi-chevron-right me-1"></i>Join as Volunteer</a></li>
                    <li><a href="${pageContext.request.contextPath}/updates"><i class="bi bi-chevron-right me-1"></i>Safety Bulletins</a></li>
                </ul>
            </div>

            <div class="col-lg-3 col-md-6">
                <h6 class="footer-heading">Disaster Types Handled</h6>
                <ul class="footer-links">
                    <li><a href="${pageContext.request.contextPath}/reports?type=FLOOD"><i class="bi bi-water me-2 text-primary"></i>Floods & Flash Inundations</a></li>
                    <li><a href="${pageContext.request.contextPath}/reports?type=EARTHQUAKE"><i class="bi bi-activity me-2 text-danger"></i>Earthquakes & Aftershocks</a></li>
                    <li><a href="${pageContext.request.contextPath}/reports?type=LANDSLIDE"><i class="bi bi-triangle-half me-2 text-warning"></i>Landslides & Mudflows</a></li>
                    <li><a href="${pageContext.request.contextPath}/reports?type=CYCLONE"><i class="bi bi-tornado me-2 text-info"></i>Cyclones & Gales</a></li>
                    <li><a href="${pageContext.request.contextPath}/reports?type=FIRE"><i class="bi bi-fire me-2 text-danger"></i>Wildfires & Urban Fires</a></li>
                </ul>
            </div>

            <div class="col-lg-3 col-md-6">
                <h6 class="footer-heading">Academic Information</h6>
                <p class="text-secondary small mb-2">
                    Developed for Final Year College Academic Project.
                </p>
                <div class="small text-secondary">
                    <div><strong>Technology Stack:</strong></div>
                    <div>&bull; Java 17 LTS / OOP Principles</div>
                    <div>&bull; Jakarta Servlet 6.0 & JSP</div>
                    <div>&bull; Apache Tomcat 10.1</div>
                    <div>&bull; MySQL 8.x + HikariCP</div>
                    <div>&bull; BCrypt Hashing</div>
                    <div>&bull; Bootstrap 5 & Chart.js</div>
                </div>
            </div>
        </div>

        <div class="footer-bottom d-flex justify-content-between align-items-center flex-wrap">
            <div>&copy; 2026 Disaster Emergency Management System. Academic Demonstration Platform.</div>
            <div class="d-flex gap-3">
                <a href="${pageContext.request.contextPath}/about" class="text-secondary text-decoration-none small">About Project</a>
                <a href="${pageContext.request.contextPath}/reports" class="text-secondary text-decoration-none small">Public Feed</a>
                <a href="${pageContext.request.contextPath}/shelters" class="text-secondary text-decoration-none small">Shelters</a>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap 5.3.3 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.2/dist/chart.umd.min.js"></script>
<!-- Custom JavaScript -->
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/charts.js"></script>
</body>
</html>
