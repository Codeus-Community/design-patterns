package org.codeus.design_patterns.observer.refactored;

import org.codeus.design_patterns.observer.common.ServerStatus;

/**
 * Simple email notification observer for demonstrating Observer Pattern.
 */
public class EmailNotificationObserver implements Observer {

    private final String emailAddress;

    public EmailNotificationObserver(String emailAddress) {
        this.emailAddress = emailAddress;
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

        // Send email only for CRITICAL issues
        if (server.getStatus() == ServerStatus.CRITICAL) {
            System.out.printf("EMAIL: Critical alert sent to %s%n", emailAddress);
            System.out.printf("  Subject: [CRITICAL] %s is down%n", server.getName());
            System.out.printf("  Body: %s%n", server.getErrorMessage());
        }
    }
}