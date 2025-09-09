package org.codeus.design_patterns.observer.refactored;

/**
 * Simple Slack notification observer for demonstrating Observer Pattern.
 */
public class SlackNotificationObserver implements Observer {

    private final String channel;

    public SlackNotificationObserver(String channel) {
        this.channel = channel;
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

        // Send Slack alert for any problematic status
        if (server.getStatus().isProblematic()) {
            System.out.printf("SLACK: Alert sent to %s%n", channel);
            System.out.printf("  Message: %s is %s - %s%n",
                    server.getName(),
                    server.getStatus(),
                    server.getErrorMessage());
        }
    }
}