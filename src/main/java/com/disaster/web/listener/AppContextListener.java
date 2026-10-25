package com.disaster.web.listener;

import com.disaster.config.DatabaseConnectionManager;
import com.disaster.config.DatabaseInitializer;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application Lifecycle Listener.
 * Automatically bootstraps database schema, connection pools, and seed records.
 */
@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("===============================================================");
        logger.info("   DISASTER MANAGEMENT PLATFORM INITIALIZING (Tomcat 10.1)    ");
        logger.info("===============================================================");
        try {
            DatabaseInitializer.initialize();
            sce.getServletContext().setAttribute("appName", "Disaster Emergency Management System");
            sce.getServletContext().setAttribute("appVersion", "1.0.0");
            logger.info("Application context successfully initialized.");
        } catch (Exception e) {
            logger.error("Error during application initialization: {}", e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutting down Disaster Management Platform...");
        DatabaseConnectionManager.getInstance().shutdown();
        logger.info("Application shutdown complete.");
    }
}

