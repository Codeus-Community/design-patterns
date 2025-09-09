package org.codeus.design_patterns.observer.refactored;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple console logging observer for demonstrating Observer Pattern.
 */
public class ConsoleLoggingObserver implements Observer {

    private final DateTimeFormatter timestampFormatter;

    public ConsoleLoggingObserver() {
        this.timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void update(Subject subject) {
        // Type safety check
        if (!(subject instanceof CleanServerMonitor monitor)) {
            return;
        }

        // Get the server that was just checked
        var server = monitor.getLastCheckedServer();

        if (server == null) {
            return;
        }

        // Log server status with timestamp
        String timestamp = LocalDateTime.now().format(timestampFormatter);
        System.out.printf("[%s] %s: %s%n", timestamp, server.getName(), server.getStatus());

        // Show error message if there's a problem
        if (server.getErrorMessage() != null) {
            System.out.printf("Error: %s%n", server.getErrorMessage());
        }
    }
}