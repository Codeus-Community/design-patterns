package org.codeus.design_patterns.observer.common;

/**
 * Enum representing different server statuses in the monitoring system.
 * 
 * - ONLINE: Server is running normally
 * - OFFLINE: Server is not responding
 * - ERROR: Server has non-critical issues
 * - CRITICAL: Server has critical issues requiring immediate attention
 */
public enum ServerStatus {
    ONLINE("Server is running normally"),
    OFFLINE("Server is not responding"),
    ERROR("Server has non-critical issues"), 
    CRITICAL("Server has critical issues requiring immediate attention");
    
    private final String description;
    
    ServerStatus(String description) {
        this.description = description;
    }
    
    /**
     * Gets the description of the server status.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if the status indicates a problematic state.
     */
    public boolean isProblematic() {
        return this == ERROR || this == CRITICAL;
    }
    
    /**
     * Checks if the status requires immediate attention.
     */
    public boolean isCritical() {
        return this == CRITICAL;
    }
}