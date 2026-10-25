package com.disaster.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Singleton Database Connection Manager demonstrating SINGLETON PATTERN and RESOURCE MANAGEMENT.
 * Manages high-performance HikariCP connection pooling for MySQL with resilience failover.
 */
public class DatabaseConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionManager.class);
    private static volatile DatabaseConnectionManager instance;
    private HikariDataSource dataSource;
    private boolean usingFallbackMode = false;
    private String activeDatabaseType = "MySQL";

    private DatabaseConnectionManager() {
        initDataSource();
    }

    public static DatabaseConnectionManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnectionManager.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionManager();
                }
            }
        }
        return instance;
    }

    private void initDataSource() {
        AppConfig config = AppConfig.getInstance();
        String driver = config.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        String url = config.getProperty("db.url", "jdbc:mysql://localhost:3306/disaster_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        String user = config.getProperty("db.username", "root");
        String pass = config.getProperty("db.password", "root");

        try {
            logger.info("Attempting primary connection to MySQL: {}", url);
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName(driver);
            hikariConfig.setJdbcUrl(url);
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(pass);
            hikariConfig.setMaximumPoolSize(config.getIntProperty("db.pool.maximumPoolSize", 10));
            hikariConfig.setMinimumIdle(config.getIntProperty("db.pool.minimumIdle", 2));
            hikariConfig.setIdleTimeout(config.getIntProperty("db.pool.idleTimeout", 300000));
            hikariConfig.setConnectionTimeout(config.getIntProperty("db.pool.connectionTimeout", 5000));
            hikariConfig.setPoolName("DisasterMySQLPool");

            this.dataSource = new HikariDataSource(hikariConfig);
            // Verify connection
            try (Connection conn = this.dataSource.getConnection()) {
                logger.info("SUCCESS: Connected to primary MySQL Database: {}", conn.getMetaData().getDatabaseProductName());
                this.usingFallbackMode = false;
                this.activeDatabaseType = "MySQL (" + conn.getMetaData().getDatabaseProductVersion() + ")";
                return;
            }
        } catch (Exception e) {
            logger.warn("Primary MySQL connection could not be established: {}", e.getMessage());
            if (this.dataSource != null) {
                this.dataSource.close();
                this.dataSource = null;
            }
        }

        // Check if fallback to H2 MySQL-mode is permitted
        if (config.getBooleanProperty("db.autoFallbackToH2", true)) {
            logger.info("Engaging Embedded Database (MySQL compatibility mode) to guarantee uninterrupted system operations.");
            try {
                String fbDriver = config.getProperty("db.fallback.driver", "org.h2.Driver");
                String fbUrl = config.getProperty("db.fallback.url", "jdbc:h2:mem:disaster_db;MODE=MySQL;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE");
                String fbUser = config.getProperty("db.fallback.username", "sa");
                String fbPass = config.getProperty("db.fallback.password", "");

                HikariConfig fbConfig = new HikariConfig();
                fbConfig.setDriverClassName(fbDriver);
                fbConfig.setJdbcUrl(fbUrl);
                fbConfig.setUsername(fbUser);
                fbConfig.setPassword(fbPass);
                fbConfig.setMaximumPoolSize(5);
                fbConfig.setPoolName("DisasterFallbackPool");

                this.dataSource = new HikariDataSource(fbConfig);
                this.usingFallbackMode = true;
                this.activeDatabaseType = "Embedded Engine (MySQL Mode)";
                logger.info("SUCCESS: Connected to Embedded Database Engine (MySQL Mode).");
            } catch (Exception ex) {
                logger.error("FATAL: Failed to initialize fallback database: {}", ex.getMessage(), ex);
                throw new RuntimeException("Could not initialize any database connection pool", ex);
            }
        } else {
            throw new RuntimeException("MySQL connection failed and fallback mode is disabled in db.properties");
        }
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            synchronized (this) {
                if (dataSource == null || dataSource.isClosed()) {
                    initDataSource();
                }
            }
        }
        return dataSource.getConnection();
    }

    public boolean isUsingFallbackMode() {
        return usingFallbackMode;
    }

    public String getActiveDatabaseType() {
        return activeDatabaseType;
    }

    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Shutting down Database Connection Pool...");
            dataSource.close();
        }
    }
}

