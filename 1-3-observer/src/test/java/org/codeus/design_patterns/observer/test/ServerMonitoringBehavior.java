package org.codeus.design_patterns.observer.test;

import org.codeus.design_patterns.observer.common.Server;

/**
 * Common interface for testing server monitoring behavior.
 * Works with both MessyServerMonitor and CleanServerMonitor implementations.
 */
public interface ServerMonitoringBehavior {
    
    /**
     * Add a server to monitoring.
     */
    void addServer(Server server);
    
    /**
     * Check a specific server.
     */
    void checkServer(Server server);
    
    /**
     * Check all servers.
     */
    void checkAllServers();
    
    /**
     * Control email notifications.
     */
    void setEmailEnabled(boolean enabled);
    
    /**
     * Control slack notifications.
     */
    void setSlackEnabled(boolean enabled);
    
    /**
     * Get current server count.
     */
    int getServerCount();
    
    /**
     * Check if email is enabled.
     */
    boolean isEmailEnabled();
    
    /**
     * Check if slack is enabled.
     */
    boolean isSlackEnabled();
}