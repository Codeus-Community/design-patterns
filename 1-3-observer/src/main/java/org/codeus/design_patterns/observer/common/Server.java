package org.codeus.design_patterns.observer.common;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This class encapsulates all the basic information about a server
 * including its identification, network details, and current status.
 */
public class Server {

    private static final int MIN_PORT = 1;
    private static final int MAX_PORT = 65535;

    private final String name;
    private final String ipAddress;
    private final int port;
    private ServerStatus status;
    private LocalDateTime lastChecked;
    private String errorMessage;

    public Server(String name, String ipAddress, int port) {
        validateInputs(name, ipAddress, port);

        this.name = name.trim();
        this.ipAddress = ipAddress.trim();
        this.port = port;
        this.status = null; // No status until first check
        this.lastChecked = null;
        this.errorMessage = null;
    }

    private void validateInputs(String name, String ipAddress, int port) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Server name cannot be null or empty");
        }
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("IP address cannot be null or empty");
        }
        if (port < MIN_PORT || port > MAX_PORT) {
            throw new IllegalArgumentException("Port must be between " + MIN_PORT + " and " + MAX_PORT);
        }
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public LocalDateTime getLastChecked() {
        return lastChecked;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Updates the server status and records the check time.
     */
    public void updateStatus(ServerStatus newStatus, String errorMessage) {
        this.status = newStatus;
        this.lastChecked = LocalDateTime.now();
        this.errorMessage = errorMessage;
    }

    /**
     * Gets a formatted string representation of the server for logging.
     */
    public String getDisplayInfo() {
        return String.format("%s (%s:%d)", name, ipAddress, port);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return port == server.port &&
                Objects.equals(name, server.name) &&
                Objects.equals(ipAddress, server.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ipAddress, port);
    }

    @Override
    public String toString() {
        return String.format("Server{%s:%d, status=%s}",
                name, port, status != null ? status : "UNKNOWN");
    }
}