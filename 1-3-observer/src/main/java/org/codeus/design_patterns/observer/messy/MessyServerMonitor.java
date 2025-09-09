package org.codeus.design_patterns.observer.messy;

import org.codeus.design_patterns.observer.common.Server;
import org.codeus.design_patterns.observer.common.ServerStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Messy server monitoring system.
 * Shows the core problem: ONE class doing TOO MANY things.
 *
 * Problems:
 * - checkServer() method does everything
 * - Hard to add new notifications
 * - Can't test individual features
 */
public class MessyServerMonitor {

    private final List<Server> servers = new ArrayList<>();
    private final Random random = new Random();

    private boolean emailEnabled = true;
    private boolean slackEnabled = true;

    public void addServer(Server server) {
        servers.add(server);
        System.out.println("Added server: " + server.getName());
    }

    /**
     * THE PROBLEM METHOD - does too many things!
     *
     * This method handles:
     * 1. Checking server health
     * 2. Console logging  
     * 3. Email notifications
     * 4. Slack notifications
     */
    public void checkServer(Server server) {
        // 1. Check server health
        ServerStatus newStatus = simulateCheck();
        ServerStatus oldStatus = server.getStatus();
        String errorMessage = generateErrorMessage(newStatus);
        server.updateStatus(newStatus, errorMessage);

        // 2. Console logging
        String oldStatusStr = oldStatus != null ? oldStatus.toString() : "UNKNOWN";
        System.out.println(server.getName() + ": " + oldStatusStr + " -> " + newStatus);

        // 3. Email notifications
        if (emailEnabled && newStatus == ServerStatus.CRITICAL) {
            System.out.println("EMAIL: Critical alert for " + server.getName());
            System.out.println("  To: admin@company.com");
            System.out.println("  Subject: Server Down!");
        }

        // 4. Slack notifications
        if (slackEnabled && newStatus.isProblematic()) {
            System.out.println("SLACK: Alert in #ops channel");
            System.out.println("  Server " + server.getName() + " has issues");
        }

        // What happens when business wants Discord notifications?
        // THIS method should be modified again!
    }

    public void checkAllServers() {
        System.out.println("=== Health Check Start ===");
        for (Server server : servers) {
            checkServer(server);
            System.out.println();
        }
        System.out.println("=== Health Check Done ===");
    }

    private ServerStatus simulateCheck() {
        int rand = random.nextInt(100);
        if (rand < 70) return ServerStatus.ONLINE;
        else if (rand < 90) return ServerStatus.ERROR;
        else return ServerStatus.CRITICAL;
    }

    private String generateErrorMessage(ServerStatus status) {
        return switch (status) {
            case ERROR -> "High response time detected";
            case CRITICAL -> "Service completely unresponsive";
            case OFFLINE -> "Server not reachable";
            default -> null;
        };
    }

    public void enableEmail(boolean enabled) {
        this.emailEnabled = enabled;
    }

    public void enableSlack(boolean enabled) {
        this.slackEnabled = enabled;
    }

    public List<Server> getServers() {
        return new ArrayList<>(servers);
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public boolean isSlackEnabled() {
        return slackEnabled;
    }
}