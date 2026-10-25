-- Disaster Management System Initial Seed Data
-- Passwords will be verified/updated by DatabaseInitializer if needed.
-- Default demo password is: password123 (and admin123 for admin)
-- Precomputed BCrypt hash for 'admin123': $2a$10$7zB3VvM4pYk7T9bXxqM1UeQ3K9y6n9Hw.G4L6eM2R7P1t5x8uK0e2
-- Precomputed BCrypt hash for 'password123': $2a$10$e88hJ8.O1fMvS8r.yY3Kie6y9mE2qN3B2H8lZ1nI3vW5gV6eK3O8m

-- 1. Default Users
-- 1: Admin
INSERT INTO users (id, username, email, password_hash, full_name, phone, address, role, status)
VALUES (1, 'admin', 'admin@disastermgmt.gov', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Disaster Control Director', '+1-800-555-0199', 'National Emergency Operation Center, Sector 1', 'ADMIN', 'ACTIVE');

-- 2: Rescue Team Lead
INSERT INTO users (id, username, email, password_hash, full_name, phone, address, role, status)
VALUES (2, 'captain_sarah', 'sarah.connor@rescueteam.org', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Capt. Sarah Jenkins', '+1-555-0144', 'Rapid Response Base Alpha, Coastal Zone', 'RESCUE_TEAM', 'ACTIVE');

-- 3: Volunteer
INSERT INTO users (id, username, email, password_hash, full_name, phone, address, role, status)
VALUES (3, 'david_volunteer', 'david.miller@gmail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'David Miller (EMT / First Aid)', '+1-555-0182', '45 Elm Street, North District', 'VOLUNTEER', 'ACTIVE');

-- 4: Citizen 1
INSERT INTO users (id, username, email, password_hash, full_name, phone, address, role, status)
VALUES (4, 'citizen_john', 'john.doe@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Johnathan Doe', '+1-555-0123', '742 Riverside Avenue, Riverdale', 'CITIZEN', 'ACTIVE');

-- 5: Citizen 2
INSERT INTO users (id, username, email, password_hash, full_name, phone, address, role, status)
VALUES (5, 'emily_watson', 'emily.w@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Emily Watson', '+1-555-0167', '12 Pine Valley Road, Hillside', 'CITIZEN', 'ACTIVE');

-- 2. Volunteer Profile
INSERT INTO volunteer_profiles (user_id, skills, availability, preferred_assignments, experience_years, location)
VALUES (3, 'Certified CPR/First Aid, Water Rescue, Drone Operator, Search & Logistics', 'AVAILABLE', 'First Aid Station, Evacuation Assistance', 4, 'North District');

-- 3. Rescue Teams
INSERT INTO rescue_teams (id, team_name, team_lead_id, contact_number, specialization, member_count, operational_area, availability)
VALUES (1, 'Task Force Alpha - Water Rescue', 2, '+1-555-0144', 'Flood & Fast Water Rescue, Amphibious Evacuation', 8, 'Coastal and River Basin Sectors', 'AVAILABLE');

INSERT INTO rescue_teams (id, team_name, team_lead_id, contact_number, specialization, member_count, operational_area, availability)
VALUES (2, 'Bravo Urban Search & K-9', 2, '+1-555-0145', 'Structural Collapse, K-9 Search, Heavy Rescue', 10, 'Metropolitan & Industrial Zones', 'AVAILABLE');

-- Rescue Team Members
INSERT INTO rescue_team_members (team_id, user_id, role_in_team)
VALUES (1, 2, 'Commander / Lead Tactician');

-- 4. Shelters
INSERT INTO shelters (id, name, address, city, contact_person, contact_phone, total_capacity, current_occupancy, has_food, has_water, has_sanitation, has_medical, has_electricity, has_bedding, status)
VALUES (1, 'Central High School Emergency Shelter', '100 Education Way', 'Riverdale', 'Principal Mark Stevens', '+1-555-0191', 350, 180, true, true, true, true, true, true, 'ACTIVE');

INSERT INTO shelters (id, name, address, city, contact_person, contact_phone, total_capacity, current_occupancy, has_food, has_water, has_sanitation, has_medical, has_electricity, has_bedding, status)
VALUES (2, 'Northside Community Sports Arena', '88 Champions Boulevard', 'North District', 'Arena Coordinator Lisa Ray', '+1-555-0192', 600, 420, true, true, true, true, true, true, 'ACTIVE');

INSERT INTO shelters (id, name, address, city, contact_person, contact_phone, total_capacity, current_occupancy, has_food, has_water, has_sanitation, has_medical, has_electricity, has_bedding, status)
VALUES (3, 'St. Jude Community Hall', '14 Hope Street', 'Hillside', 'Father Thomas Kelly', '+1-555-0193', 150, 45, true, true, true, false, true, true, 'ACTIVE');

INSERT INTO shelters (id, name, address, city, contact_person, contact_phone, total_capacity, current_occupancy, has_food, has_water, has_sanitation, has_medical, has_electricity, has_bedding, status)
VALUES (4, 'South Bay Evacuation Center', '500 Harbor Drive', 'South Coast', 'Chief Robert Vance', '+1-555-0194', 400, 400, true, true, true, true, true, true, 'FULL');

-- 5. Relief Resources
INSERT INTO relief_resources (id, name, category, quantity, unit, storage_location, min_stock_level)
VALUES (1, 'Ready-to-Eat Emergency Food Rations', 'FOOD_WATER', 2500, 'packs', 'Central Warehouse Hub A', 500);

INSERT INTO relief_resources (id, name, category, quantity, unit, storage_location, min_stock_level)
VALUES (2, 'Purified Drinking Water (5L Jugs)', 'FOOD_WATER', 3200, 'jugs', 'Central Warehouse Hub A', 600);

INSERT INTO relief_resources (id, name, category, quantity, unit, storage_location, min_stock_level)
VALUES (3, 'Trauma & Emergency First-Aid Kits', 'MEDICAL', 340, 'kits', 'Medical Depot Sector 2', 80);

INSERT INTO relief_resources (id, name, category, quantity, unit, storage_location, min_stock_level)
VALUES (4, 'Thermal Emergency Blankets', 'CLOTHING_BEDDING', 1800, 'blankets', 'Logistics Depot North', 300);

INSERT INTO relief_resources (id, name, category, quantity, unit, storage_location, min_stock_level)
VALUES (5, 'Inflatable Rapid Rescue Rafts', 'RESCUE_EQUIPMENT', 28, 'units', 'Water Rescue Bay 1', 10);

INSERT INTO relief_resources (id, name, category, quantity, unit, storage_location, min_stock_level)
VALUES (6, 'Emergency Sanitation & Hygiene Packs', 'SANITATION', 1200, 'packs', 'Central Warehouse Hub B', 250);

INSERT INTO relief_resources (id, name, category, quantity, unit, storage_location, min_stock_level)
VALUES (7, 'Heavy-Duty Water Drainage Pumps', 'RESCUE_EQUIPMENT', 8, 'units', 'Central Equipment Depot', 12);

-- 6. Disaster Reports
INSERT INTO disaster_reports (id, report_code, reporter_id, disaster_type, title, description, location, landmark, severity, status, incident_date)
VALUES (1, 'REP-2026-1001', 4, 'FLOOD', 'Severe Flash Flooding near Riverdale Bridge', 'River water breached floodwalls following torrential rains. Over 3 feet of water on residential roadways. Multiple vehicles stranded.', 'Riverdale Avenue, Sectors 4-6', 'Near Riverdale Old Bridge', 'HIGH', 'VERIFIED', CURRENT_TIMESTAMP);

INSERT INTO disaster_reports (id, report_code, reporter_id, disaster_type, title, description, location, landmark, severity, status, incident_date)
VALUES (2, 'REP-2026-1002', 5, 'LANDSLIDE', 'Hillside Road Blocked by Mudslide', 'Massive mud and rockslide has severed the main access road connecting Hillside to Highway 9. Power lines downed.', 'Hillside Pass, Mile Marker 14', 'Opposite Pine Valley Lookout', 'CRITICAL', 'RESPONDING', CURRENT_TIMESTAMP);

INSERT INTO disaster_reports (id, report_code, reporter_id, disaster_type, title, description, location, landmark, severity, status, incident_date)
VALUES (3, 'REP-2026-1003', 4, 'FIRE', 'Commercial Warehouse Fire - Smoke Hazard', 'Major structural fire emitting heavy black smoke. Fire engines on scene, nearby residents experiencing breathing discomfort.', 'Industrial Zone East, Block C', 'Beside Central Grain Silo', 'HIGH', 'RESPONDING', CURRENT_TIMESTAMP);

INSERT INTO disaster_reports (id, report_code, reporter_id, disaster_type, title, description, location, landmark, severity, status, incident_date)
VALUES (4, 'REP-2026-1004', 5, 'CYCLONE', 'Downed Trees and Roof Damage Post Cyclone Gale', 'High-velocity gusts caused massive trees to fall onto suburban houses and roads. Street lights damaged.', 'Seaside Boulevard, South Coast', 'Near Coastal Lighthouse', 'MEDIUM', 'PENDING', CURRENT_TIMESTAMP);

-- 7. Assistance Requests
INSERT INTO assistance_requests (id, request_code, citizen_id, contact_name, contact_phone, contact_email, disaster_type, location, landmark, people_affected, assistance_type, priority, special_needs, assigned_team_id, status)
VALUES (1, 'REQ-2026-5001', 4, 'Johnathan Doe', '+1-555-0123', 'john.doe@email.com', 'FLOOD', '742 Riverside Avenue, Riverdale', 'Yellow two-story house with flooded porch', 4, 'EVACUATION', 'CRITICAL', 'Includes elderly grandmother (requires wheelchair) and 6-month-old infant. Water rising fast.', 1, 'IN_PROGRESS');

INSERT INTO assistance_requests (id, request_code, citizen_id, contact_name, contact_phone, contact_email, disaster_type, location, landmark, people_affected, assistance_type, priority, special_needs, assigned_team_id, status)
VALUES (2, 'REQ-2026-5002', 5, 'Emily Watson', '+1-555-0167', 'emily.w@email.com', 'LANDSLIDE', '12 Pine Valley Road, Hillside', 'Near blue water tank', 2, 'SEARCH_RESCUE', 'HIGH', 'Debris blocking house exit, neighbor trapped in garage.', 2, 'ASSIGNED');

INSERT INTO assistance_requests (id, request_code, citizen_id, contact_name, contact_phone, contact_email, disaster_type, location, landmark, people_affected, assistance_type, priority, special_needs, assigned_team_id, status)
VALUES (3, 'REQ-2026-5003', 4, 'Marcus Sterling', '+1-555-0188', 'marcus.s@email.com', 'FLOOD', '55 Market Street, Riverdale', 'Above Grocery Store', 6, 'FOOD_WATER', 'MEDIUM', 'Stranded on upper floor, no drinking water or power for 24 hours.', NULL, 'PENDING');

-- 8. Disaster Updates (Official Advisories)
INSERT INTO disaster_updates (id, title, content, disaster_type, severity, affected_areas, issued_by)
VALUES (1, 'URGENT: Flood Evacuation Warning for Riverdale Basin', 'River levels have exceeded critical flood stage. Citizens in low-lying Riverdale sectors are advised to evacuate immediately to Central High School or Northside Arena. Avoid driving through flooded streets.', 'FLOOD', 'CRITICAL', 'Riverdale Sectors 1-6, Lowland Valley', 1);

INSERT INTO disaster_updates (id, title, content, disaster_type, severity, affected_areas, issued_by)
VALUES (2, 'Hillside Pass Highway 9 Closure Notice', 'Hillside Pass remains impassable due to landslides and fallen debris. Emergency clearing crews and Task Force Bravo are deployed. Use Southern Bypass route until further notice.', 'LANDSLIDE', 'WARNING', 'Hillside, Northern Corridor, Highway 9', 1);

INSERT INTO disaster_updates (id, title, content, disaster_type, severity, affected_areas, issued_by)
VALUES (3, 'Cyclone Relief Shelters Operating at Full Readiness', 'All district relief shelters are equipped with food, water, sanitation, and medical first responders. Call 1-800-555-DISASTER for immediate evacuation support.', 'CYCLONE', 'INFORMATION', 'All Districts', 1);

-- 9. Incident Assignments
INSERT INTO incident_assignments (volunteer_id, report_id, request_id, task_title, task_description, assigned_by, status)
VALUES (3, 1, 1, 'Riverdale Evacuation Center First-Aid & Registration', 'Assist arriving flood evacuees with preliminary medical triage and family registration.', 1, 'IN_PROGRESS');
