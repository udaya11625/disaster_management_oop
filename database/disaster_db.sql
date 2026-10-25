-- ========================================================
-- DISASTER MANAGEMENT SYSTEM - COMPLETE DATABASE SCRIPT
-- MySQL 8.x Compatible
-- ========================================================

DROP DATABASE IF EXISTS disaster_db;
CREATE DATABASE disaster_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE disaster_db;

-- 1. USERS TABLE
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255),
    role ENUM('CITIZEN', 'VOLUNTEER', 'RESCUE_TEAM', 'ADMIN') NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. VOLUNTEER PROFILES
CREATE TABLE volunteer_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    skills TEXT,
    availability VARCHAR(30) DEFAULT 'AVAILABLE',
    preferred_assignments VARCHAR(255),
    experience_years INT DEFAULT 0,
    location VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. RESCUE TEAMS
CREATE TABLE rescue_teams (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_name VARCHAR(100) NOT NULL UNIQUE,
    team_lead_id BIGINT,
    contact_number VARCHAR(20) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    member_count INT DEFAULT 5,
    operational_area VARCHAR(100) NOT NULL,
    availability VARCHAR(30) DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (team_lead_id) REFERENCES users(id) ON DELETE SET NULL
);

-- 4. RESCUE TEAM MEMBERS
CREATE TABLE rescue_team_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role_in_team VARCHAR(50) DEFAULT 'Rescue Specialist',
    FOREIGN KEY (team_id) REFERENCES rescue_teams(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 5. DISASTER REPORTS
CREATE TABLE disaster_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_code VARCHAR(30) NOT NULL UNIQUE,
    reporter_id BIGINT,
    disaster_type VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(255) NOT NULL,
    landmark VARCHAR(255),
    latitude DOUBLE DEFAULT NULL,
    longitude DOUBLE DEFAULT NULL,
    severity VARCHAR(20) NOT NULL,
    image_url VARCHAR(500),
    status VARCHAR(30) DEFAULT 'PENDING',
    verified_by BIGINT,
    admin_notes TEXT,
    incident_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (verified_by) REFERENCES users(id) ON DELETE SET NULL
);

-- 6. EMERGENCY ASSISTANCE REQUESTS
CREATE TABLE assistance_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_code VARCHAR(30) NOT NULL UNIQUE,
    citizen_id BIGINT,
    contact_name VARCHAR(100) NOT NULL,
    contact_phone VARCHAR(20) NOT NULL,
    contact_email VARCHAR(100),
    disaster_type VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    landmark VARCHAR(255),
    people_affected INT NOT NULL DEFAULT 1,
    assistance_type VARCHAR(50) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    special_needs TEXT,
    assigned_team_id BIGINT,
    status VARCHAR(30) DEFAULT 'PENDING',
    resolution_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (citizen_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (assigned_team_id) REFERENCES rescue_teams(id) ON DELETE SET NULL
);

-- 7. SHELTERS
CREATE TABLE shelters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100) NOT NULL,
    contact_phone VARCHAR(20) NOT NULL,
    total_capacity INT NOT NULL,
    current_occupancy INT NOT NULL DEFAULT 0,
    has_food BOOLEAN DEFAULT TRUE,
    has_water BOOLEAN DEFAULT TRUE,
    has_sanitation BOOLEAN DEFAULT TRUE,
    has_medical BOOLEAN DEFAULT FALSE,
    has_electricity BOOLEAN DEFAULT TRUE,
    has_bedding BOOLEAN DEFAULT TRUE,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 8. SHELTER ALLOCATIONS
CREATE TABLE shelter_allocations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    shelter_id BIGINT NOT NULL,
    representative_name VARCHAR(100) NOT NULL,
    contact_phone VARCHAR(20) NOT NULL,
    people_count INT NOT NULL,
    check_in_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    check_out_date TIMESTAMP NULL,
    status VARCHAR(20) DEFAULT 'CHECKED_IN',
    notes TEXT,
    FOREIGN KEY (shelter_id) REFERENCES shelters(id) ON DELETE CASCADE
);

-- 9. RELIEF RESOURCES (INVENTORY)
CREATE TABLE relief_resources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    unit VARCHAR(30) NOT NULL,
    storage_location VARCHAR(150) NOT NULL,
    min_stock_level INT NOT NULL DEFAULT 50,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 10. RESOURCE DISTRIBUTIONS
CREATE TABLE resource_distributions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    destination_type VARCHAR(30) NOT NULL,
    destination_name VARCHAR(150) NOT NULL,
    shelter_id BIGINT,
    request_id BIGINT,
    distributed_by BIGINT,
    recipient_contact VARCHAR(100),
    distribution_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remarks TEXT,
    FOREIGN KEY (resource_id) REFERENCES relief_resources(id) ON DELETE RESTRICT,
    FOREIGN KEY (shelter_id) REFERENCES shelters(id) ON DELETE SET NULL,
    FOREIGN KEY (request_id) REFERENCES assistance_requests(id) ON DELETE SET NULL,
    FOREIGN KEY (distributed_by) REFERENCES users(id) ON DELETE SET NULL
);

-- 11. DISASTER UPDATES & ADVISORIES
CREATE TABLE disaster_updates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    disaster_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    affected_areas VARCHAR(255) NOT NULL,
    issued_by BIGINT,
    published_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (issued_by) REFERENCES users(id) ON DELETE SET NULL
);

-- 12. INCIDENT ASSIGNMENTS (VOLUNTEER TASKS)
CREATE TABLE incident_assignments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    volunteer_id BIGINT NOT NULL,
    report_id BIGINT,
    request_id BIGINT,
    task_title VARCHAR(150) NOT NULL,
    task_description TEXT,
    assigned_by BIGINT,
    status VARCHAR(30) DEFAULT 'ASSIGNED',
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    FOREIGN KEY (volunteer_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES disaster_reports(id) ON DELETE CASCADE,
    FOREIGN KEY (request_id) REFERENCES assistance_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_by) REFERENCES users(id) ON DELETE SET NULL
);

-- ========================================================
-- SEED DATA
-- Default Demo Password for all accounts: password123
-- (Admin also accepts admin123)
-- BCrypt Hash: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi
-- ========================================================

-- Users
INSERT INTO users (id, username, email, password_hash, full_name, phone, address, role, status) VALUES
(1, 'admin', 'admin@disastermgmt.gov', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Disaster Control Director', '+1-800-555-0199', 'National Emergency Operation Center, Sector 1', 'ADMIN', 'ACTIVE'),
(2, 'captain_sarah', 'sarah.connor@rescueteam.org', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Capt. Sarah Jenkins', '+1-555-0144', 'Rapid Response Base Alpha, Coastal Zone', 'RESCUE_TEAM', 'ACTIVE'),
(3, 'david_volunteer', 'david.miller@gmail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'David Miller (EMT / First Aid)', '+1-555-0182', '45 Elm Street, North District', 'VOLUNTEER', 'ACTIVE'),
(4, 'citizen_john', 'john.doe@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Johnathan Doe', '+1-555-0123', '742 Riverside Avenue, Riverdale', 'CITIZEN', 'ACTIVE'),
(5, 'emily_watson', 'emily.w@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Emily Watson', '+1-555-0167', '12 Pine Valley Road, Hillside', 'CITIZEN', 'ACTIVE');

-- Volunteer Profile
INSERT INTO volunteer_profiles (user_id, skills, availability, preferred_assignments, experience_years, location) VALUES
(3, 'Certified CPR/First Aid, Water Rescue, Drone Operator, Search & Logistics', 'AVAILABLE', 'First Aid Station, Evacuation Assistance', 4, 'North District');

-- Rescue Teams
INSERT INTO rescue_teams (id, team_name, team_lead_id, contact_number, specialization, member_count, operational_area, availability) VALUES
(1, 'Task Force Alpha - Water Rescue', 2, '+1-555-0144', 'Flood & Fast Water Rescue, Amphibious Evacuation', 8, 'Coastal and River Basin Sectors', 'AVAILABLE'),
(2, 'Bravo Urban Search & K-9', 2, '+1-555-0145', 'Structural Collapse, K-9 Search, Heavy Rescue', 10, 'Metropolitan & Industrial Zones', 'AVAILABLE');

INSERT INTO rescue_team_members (team_id, user_id, role_in_team) VALUES
(1, 2, 'Commander / Lead Tactician');

-- Shelters
INSERT INTO shelters (id, name, address, city, contact_person, contact_phone, total_capacity, current_occupancy, has_food, has_water, has_sanitation, has_medical, has_electricity, has_bedding, status) VALUES
(1, 'Central High School Emergency Shelter', '100 Education Way', 'Riverdale', 'Principal Mark Stevens', '+1-555-0191', 350, 180, true, true, true, true, true, true, 'ACTIVE'),
(2, 'Northside Community Sports Arena', '88 Champions Boulevard', 'North District', 'Arena Coordinator Lisa Ray', '+1-555-0192', 600, 420, true, true, true, true, true, true, 'ACTIVE'),
(3, 'St. Jude Community Hall', '14 Hope Street', 'Hillside', 'Father Thomas Kelly', '+1-555-0193', 150, 45, true, true, true, false, true, true, 'ACTIVE'),
(4, 'South Bay Evacuation Center', '500 Harbor Drive', 'South Coast', 'Chief Robert Vance', '+1-555-0194', 400, 400, true, true, true, true, true, true, 'FULL');

-- Relief Resources
INSERT INTO relief_resources (id, name, category, quantity, unit, storage_location, min_stock_level) VALUES
(1, 'Ready-to-Eat Emergency Food Rations', 'FOOD_WATER', 2500, 'packs', 'Central Warehouse Hub A', 500),
(2, 'Purified Drinking Water (5L Jugs)', 'FOOD_WATER', 3200, 'jugs', 'Central Warehouse Hub A', 600),
(3, 'Trauma & Emergency First-Aid Kits', 'MEDICAL', 340, 'kits', 'Medical Depot Sector 2', 80),
(4, 'Thermal Emergency Blankets', 'CLOTHING_BEDDING', 1800, 'blankets', 'Logistics Depot North', 300),
(5, 'Inflatable Rapid Rescue Rafts', 'RESCUE_EQUIPMENT', 28, 'units', 'Water Rescue Bay 1', 10),
(6, 'Emergency Sanitation & Hygiene Packs', 'SANITATION', 1200, 'packs', 'Central Warehouse Hub B', 250),
(7, 'Heavy-Duty Water Drainage Pumps', 'RESCUE_EQUIPMENT', 8, 'units', 'Central Equipment Depot', 12);

-- Disaster Reports
INSERT INTO disaster_reports (id, report_code, reporter_id, disaster_type, title, description, location, landmark, severity, status, incident_date) VALUES
(1, 'REP-2026-1001', 4, 'FLOOD', 'Severe Flash Flooding near Riverdale Bridge', 'River water breached floodwalls following torrential rains. Over 3 feet of water on residential roadways. Multiple vehicles stranded.', 'Riverdale Avenue, Sectors 4-6', 'Near Riverdale Old Bridge', 'HIGH', 'VERIFIED', CURRENT_TIMESTAMP),
(2, 'REP-2026-1002', 5, 'LANDSLIDE', 'Hillside Road Blocked by Mudslide', 'Massive mud and rockslide has severed the main access road connecting Hillside to Highway 9. Power lines downed.', 'Hillside Pass, Mile Marker 14', 'Opposite Pine Valley Lookout', 'CRITICAL', 'RESPONDING', CURRENT_TIMESTAMP),
(3, 'REP-2026-1003', 4, 'FIRE', 'Commercial Warehouse Fire - Smoke Hazard', 'Major structural fire emitting heavy black smoke. Fire engines on scene, nearby residents experiencing breathing discomfort.', 'Industrial Zone East, Block C', 'Beside Central Grain Silo', 'HIGH', 'RESPONDING', CURRENT_TIMESTAMP),
(4, 'REP-2026-1004', 5, 'CYCLONE', 'Downed Trees and Roof Damage Post Cyclone Gale', 'High-velocity gusts caused massive trees to fall onto suburban houses and roads. Street lights damaged.', 'Seaside Boulevard, South Coast', 'Near Coastal Lighthouse', 'MEDIUM', 'PENDING', CURRENT_TIMESTAMP);

-- Assistance Requests
INSERT INTO assistance_requests (id, request_code, citizen_id, contact_name, contact_phone, contact_email, disaster_type, location, landmark, people_affected, assistance_type, priority, special_needs, assigned_team_id, status) VALUES
(1, 'REQ-2026-5001', 4, 'Johnathan Doe', '+1-555-0123', 'john.doe@email.com', 'FLOOD', '742 Riverside Avenue, Riverdale', 'Yellow two-story house with flooded porch', 4, 'EVACUATION', 'CRITICAL', 'Includes elderly grandmother (requires wheelchair) and 6-month-old infant. Water rising fast.', 1, 'IN_PROGRESS'),
(2, 'REQ-2026-5002', 5, 'Emily Watson', '+1-555-0167', 'emily.w@email.com', 'LANDSLIDE', '12 Pine Valley Road, Hillside', 'Near blue water tank', 2, 'SEARCH_RESCUE', 'HIGH', 'Debris blocking house exit, neighbor trapped in garage.', 2, 'ASSIGNED'),
(3, 'REQ-2026-5003', 4, 'Marcus Sterling', '+1-555-0188', 'marcus.s@email.com', 'FLOOD', '55 Market Street, Riverdale', 'Above Grocery Store', 6, 'FOOD_WATER', 'MEDIUM', 'Stranded on upper floor, no drinking water or power for 24 hours.', NULL, 'PENDING');

-- Disaster Updates
INSERT INTO disaster_updates (id, title, content, disaster_type, severity, affected_areas, issued_by) VALUES
(1, 'URGENT: Flood Evacuation Warning for Riverdale Basin', 'River levels have exceeded critical flood stage. Citizens in low-lying Riverdale sectors are advised to evacuate immediately to Central High School or Northside Arena. Avoid driving through flooded streets.', 'FLOOD', 'CRITICAL', 'Riverdale Sectors 1-6, Lowland Valley', 1),
(2, 'Hillside Pass Highway 9 Closure Notice', 'Hillside Pass remains impassable due to landslides and fallen debris. Emergency clearing crews and Task Force Bravo are deployed. Use Southern Bypass route until further notice.', 'LANDSLIDE', 'WARNING', 'Hillside, Northern Corridor, Highway 9', 1),
(3, 'Cyclone Relief Shelters Operating at Full Readiness', 'All district relief shelters are equipped with food, water, sanitation, and medical first responders. Call 1-800-555-DISASTER for immediate evacuation support.', 'CYCLONE', 'INFORMATION', 'All Districts', 1);

-- Incident Assignments
INSERT INTO incident_assignments (volunteer_id, report_id, request_id, task_title, task_description, assigned_by, status) VALUES
(3, 1, 1, 'Riverdale Evacuation Center First-Aid & Registration', 'Assist arriving flood evacuees with preliminary medical triage and family registration.', 1, 'IN_PROGRESS');

