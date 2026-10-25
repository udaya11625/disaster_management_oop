package com.disaster.runner;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * Standalone Embedded Tomcat 10.1 Bootstrapper.
 * Allows launching the entire web application directly via 'mvn exec:java' or 'java StandaloneServer'
 * while maintaining 100% standard WAR packaging compliance for production Apache Tomcat 10.1 deployments.
 */
public class StandaloneServer {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String portProp = System.getProperty("server.port");
        if (portProp != null) {
            try {
                port = Integer.parseInt(portProp);
            } catch (NumberFormatException ignored) {}
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir("target/tomcat");
        tomcat.getConnector(); // Initialize HTTP default connector

        String webappDirLocation = "src/main/webapp";
        File webappDir = new File(webappDirLocation);
        if (!webappDir.exists()) {
            webappDirLocation = ".";
            webappDir = new File(webappDirLocation);
        }

        Context ctx = tomcat.addWebapp("", webappDir.getAbsolutePath());
        ctx.setParentClassLoader(StandaloneServer.class.getClassLoader());

        File additionWebInfClasses = new File("target/classes");
        if (additionWebInfClasses.exists()) {
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                    additionWebInfClasses.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        }

        System.out.println("==================================================================");
        System.out.println("   DISASTER EMERGENCY MANAGEMENT PLATFORM (Apache Tomcat 10.1)    ");
        System.out.println("==================================================================");
        System.out.println(">> Web Application available at: http://localhost:" + port);
        System.out.println(">> Administrator Demo Credentials:  admin / admin123");
        System.out.println(">> Citizen Demo Credentials:        citizen_john / password123");
        System.out.println(">> Volunteer Demo Credentials:      david_volunteer / password123");
        System.out.println(">> Rescue Team Demo Credentials:    captain_sarah / password123");
        System.out.println("==================================================================");

        tomcat.start();
        tomcat.getServer().await();
    }
}

