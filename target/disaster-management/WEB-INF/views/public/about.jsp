<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="About Us - Disaster Emergency Management System" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/emergency-banner.jsp" %>
<%@ include file="/WEB-INF/views/common/navbar.jsp" %>

<main class="py-5">
    <div class="container">
        <!-- Breadcrumb & Header -->
        <div class="mb-4">
            <span class="badge bg-primary text-uppercase px-3 py-2 mb-2">College Academic Project</span>
            <h1 class="fw-bold text-dark">About the Disaster Management Platform</h1>
            <p class="text-muted lead">An enterprise Java-based centralized platform engineered to demonstrate robust Object-Oriented Programming (OOP) design patterns, Jakarta EE 10 Servlets, Apache Tomcat 10.1, and MySQL.</p>
        </div>

        <!-- Academic Disclaimer Callout -->
        <div class="alert alert-warning border-warning shadow-sm p-4 rounded-3 mb-5">
            <h5 class="fw-bold text-dark"><i class="bi bi-shield-exclamation me-2"></i>Official Academic Project Notice</h5>
            <p class="mb-0">
                This software application is built strictly as an academic submission for college evaluation. Real-world disaster situations require contacting government emergency authorities immediately via <strong>911</strong> or <strong>112</strong>.
            </p>
        </div>

        <!-- Four OOP Principles Breakdown Cards -->
        <h3 class="fw-bold text-dark mb-4"><i class="bi bi-code-slash text-danger me-2"></i>Java OOP Concepts Demonstrated in Code</h3>
        <div class="row g-4 mb-5">
            <!-- 1. Encapsulation -->
            <div class="col-md-6">
                <div class="card card-theme h-100 border-start border-4 border-primary">
                    <div class="card-body">
                        <div class="d-flex align-items-center mb-3">
                            <span class="badge bg-primary p-2 me-2"><i class="bi bi-box-seam fs-5"></i></span>
                            <h5 class="fw-bold text-primary mb-0">1. Encapsulation</h5>
                        </div>
                        <p class="text-secondary small">
                            All entity state is encapsulated with <code>private</code> access modifiers. State mutations enforce strict business invariants:
                        </p>
                        <ul class="text-secondary small mb-0">
                            <li><strong>Shelter Capacity:</strong> <code>shelter.canAccommodate(count)</code> and <code>shelter.allocate(count)</code> prevent over-allocation.</li>
                            <li><strong>Inventory Invariant:</strong> <code>resource.deductStock(qty)</code> prevents negative stock values.</li>
                            <li><strong>Security:</strong> User credentials are protected using salted BCrypt hashing and encapsulated in <code>PasswordUtils</code>.</li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- 2. Inheritance -->
            <div class="col-md-6">
                <div class="card card-theme h-100 border-start border-4 border-danger">
                    <div class="card-body">
                        <div class="d-flex align-items-center mb-3">
                            <span class="badge bg-danger p-2 me-2"><i class="bi bi-diagram-3 fs-5"></i></span>
                            <h5 class="fw-bold text-danger mb-0">2. Inheritance</h5>
                        </div>
                        <p class="text-secondary small">
                            Hierarchical domain models eliminate redundant code and promote clean type reuse:
                        </p>
                        <ul class="text-secondary small mb-0">
                            <li><code>BaseEntity</code> &rarr; parent class for all persistent models providing <code>id</code>, <code>createdAt</code>, and <code>updatedAt</code>.</li>
                            <li><code>User</code> (abstract) &rarr; specialized by <code>Citizen</code>, <code>Volunteer</code>, <code>RescueTeamMember</code>, and <code>Admin</code>.</li>
                            <li><code>IncidentRecord</code> (abstract) &rarr; specialized by <code>DisasterReport</code> and <code>AssistanceRequest</code>.</li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- 3. Polymorphism -->
            <div class="col-md-6">
                <div class="card card-theme h-100 border-start border-4 border-warning">
                    <div class="card-body">
                        <div class="d-flex align-items-center mb-3">
                            <span class="badge bg-warning text-dark p-2 me-2"><i class="bi bi-bezier2 fs-5"></i></span>
                            <h5 class="fw-bold text-dark mb-0">3. Polymorphism</h5>
                        </div>
                        <p class="text-secondary small">
                            Dynamic method dispatch enables uniform handling across diverse subtypes:
                        </p>
                        <ul class="text-secondary small mb-0">
                            <li><strong>Dynamic Routing:</strong> <code>user.getDashboardUrl()</code> routes dynamically to <code>/admin/dashboard</code>, <code>/rescue/dashboard</code>, etc.</li>
                            <li><strong>Role Badges:</strong> <code>user.getRoleBadge()</code> renders customized HTML styling based on concrete role type.</li>
                            <li><strong>Generic DAOs:</strong> <code>GenericDAO&lt;T, ID&gt;</code> provides a unified abstraction for all domain database interactions.</li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- 4. Abstraction -->
            <div class="col-md-6">
                <div class="card card-theme h-100 border-start border-4 border-success">
                    <div class="card-body">
                        <div class="d-flex align-items-center mb-3">
                            <span class="badge bg-success p-2 me-2"><i class="bi bi-layers fs-5"></i></span>
                            <h5 class="fw-bold text-success mb-0">4. Abstraction</h5>
                        </div>
                        <p class="text-secondary small">
                            Separation of concerns through layered enterprise architectural boundaries:
                        </p>
                        <ul class="text-secondary small mb-0">
                            <li><strong>Layered Flow:</strong> Servlets (Web Layer) &rarr; Services (Business Logic) &rarr; DAOs (Data Access) &rarr; MySQL Database.</li>
                            <li><strong>Abstract Boilerplate:</strong> <code>AbstractDAO&lt;T&gt;</code> abstracts away connection pooling, statement execution, and transaction rollback mechanics.</li>
                            <li><strong>Security Filtering:</strong> <code>AuthFilter</code> decouples authorization policy from individual servlets.</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <!-- Technical Specifications Table -->
        <div class="card card-theme mb-5">
            <div class="card-header">
                <span class="fw-bold fs-6"><i class="bi bi-cpu-fill text-primary me-2"></i>Technology Stack & Environmental Specifications</span>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-custom table-hover">
                        <tbody>
                            <tr>
                                <th style="width: 25%;">Programming Language</th>
                                <td>Java 17 LTS (Compiled with strict Java 17 bytecode compatibility)</td>
                            </tr>
                            <tr>
                                <th>Framework & API</th>
                                <td>Jakarta Servlet 6.0 & Jakarta Server Pages (JSP) 3.1 <em>(No Spring Boot)</em></td>
                            </tr>
                            <tr>
                                <th>Target Web Container</th>
                                <td>Apache Tomcat 10.1 (Direct WAR deployment + optional embedded runner)</td>
                            </tr>
                            <tr>
                                <th>Database & Connection Pool</th>
                                <td>MySQL 8.x / MariaDB with HikariCP high-performance connection pooling</td>
                            </tr>
                            <tr>
                                <th>Security & Encryption</th>
                                <td>BCrypt Password Hashing with cryptographic salting (jBCrypt 0.4)</td>
                            </tr>
                            <tr>
                                <th>Frontend UI & UX</th>
                                <td>HTML5, CSS3, JavaScript, Bootstrap 5.3.3, Bootstrap Icons, Chart.js</td>
                            </tr>
                            <tr>
                                <th>Build & Packaging Tool</th>
                                <td>Apache Maven (Standard WAR packaging: <code>mvn clean package</code>)</td>
                            </tr>
                            <tr>
                                <th>Version Control</th>
                                <td>Git and GitHub</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Emergency Operational Roles -->
        <div class="card card-theme">
            <div class="card-header">
                <span class="fw-bold fs-6"><i class="bi bi-people-fill text-danger me-2"></i>Supported Platform Roles</span>
            </div>
            <div class="card-body">
                <div class="row g-4">
                    <div class="col-md-3">
                        <div class="border rounded p-3 text-center h-100 bg-light">
                            <i class="bi bi-person-fill text-primary fs-1 mb-2"></i>
                            <h6 class="fw-bold text-dark">Citizen</h6>
                            <p class="small text-muted mb-0">Reports disaster occurrences, submits emergency assistance SOS requests, and locates available public evacuation shelters.</p>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="border rounded p-3 text-center h-100 bg-light">
                            <i class="bi bi-heart-fill text-success fs-1 mb-2"></i>
                            <h6 class="fw-bold text-dark">Volunteer</h6>
                            <p class="small text-muted mb-0">Registers specialized skills (medical, evacuation, logistics), accepts community response tasks, and logs field progress.</p>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="border rounded p-3 text-center h-100 bg-light">
                            <i class="bi bi-shield-shaded text-info fs-1 mb-2"></i>
                            <h6 class="fw-bold text-dark">Rescue Squad</h6>
                            <p class="small text-muted mb-0">Rapid response teams dispatched to critical emergency rescues, managing on-site extractions and medical evacuations.</p>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="border rounded p-3 text-center h-100 bg-light">
                            <i class="bi bi-shield-lock-fill text-danger fs-1 mb-2"></i>
                            <h6 class="fw-bold text-dark">Administrator</h6>
                            <p class="small text-muted mb-0">Central emergency director allocating rescue teams, managing shelters, overseeing relief resource inventory, and issuing public advisories.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

