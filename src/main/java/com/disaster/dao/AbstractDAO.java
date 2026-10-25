package com.disaster.dao;

import com.disaster.config.DatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Abstract DAO superclass implementing common JDBC resource handling.
 * Demonstrates INHERITANCE, ABSTRACTION, and TEMPLATE METHOD patterns.
 */
public abstract class AbstractDAO<T> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final DatabaseConnectionManager connectionManager;

    public AbstractDAO() {
        this.connectionManager = DatabaseConnectionManager.getInstance();
    }

    protected Connection getConnection() throws SQLException {
        return connectionManager.getConnection();
    }

    /**
     * Maps the current row of the ResultSet to an entity instance.
     * Demonstrates ABSTRACTION and POLYMORPHISM in subclass implementations.
     */
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    protected void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                logger.warn("Failed to close JDBC resource quietly: {}", e.getMessage());
            }
        }
    }

    protected void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                logger.warn("Failed to rollback transaction quietly: {}", e.getMessage());
            }
        }
    }
}

