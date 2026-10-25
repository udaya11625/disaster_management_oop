-- Disaster Management System Database Schema
-- Compatible with MySQL 8.x / MariaDB and H2 (MySQL Mode)

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255),
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS volunteer_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    skills TEXT,
    availability VARCHAR(30) DEFAULT 'AVAILABLE',
    preferred_assignments VARCHAR(255),
    experience_years INT DEFAULT 0,
    location VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS rescue_teams (
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

CREATE TABLE IF NOT EXISTS rescue_team_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role_in_team VARCHAR(50) DEFAULT 'Rescue Specialist',
    FOREIGN KEY (team_id) REFERENCES rescue_teams(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS disaster_reports (
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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (verified_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS assistance_requests (
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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (citizen_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (assigned_team_id) REFERENCES rescue_teams(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS shelters (
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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shelter_allocations (
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

CREATE TABLE IF NOT EXISTS relief_resources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    unit VARCHAR(30) NOT NULL,
    storage_location VARCHAR(150) NOT NULL,
    min_stock_level INT NOT NULL DEFAULT 50,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS resource_distributions (
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

CREATE TABLE IF NOT EXISTS disaster_updates (
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

CREATE TABLE IF NOT EXISTS incident_assignments (
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
