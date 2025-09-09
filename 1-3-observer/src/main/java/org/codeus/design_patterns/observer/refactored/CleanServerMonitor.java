package org.codeus.design_patterns.observer.refactored;

import org.codeus.design_patterns.observer.common.Server;
import org.codeus.design_patterns.observer.common.ServerStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clean server monitoring system using Observer Pattern.
 */
public class CleanServerMonitor implements Subject {

    private final List<Server> servers;
    private final List<Observer> observers;
    private final Random random;

    // Simple Pull Model state - stores the server that was just checked
    private Server lastCheckedServer;

    public CleanServerMonitor() {
        this.servers = new ArrayList<>();
        this.observers = new CopyOnWriteArrayList<>();
        this.random = new Random();
        this.lastCheckedServer = null;
    }

    /**
     * Adds a server to the monitoring list.
     */
    public void addServer(Server server) {
        servers.add(server);
        System.out.println("Added server to monitoring: " + server.getDisplayInfo());
    }

    /**
     * Removes a server from the monitoring list.
     */
    public void removeServer(Server server) {
        servers.remove(server);
        System.out.println("Removed server from monitoring: " + server.getDisplayInfo());
    }

    /**
     * Checks the status of a specific server.
     */
    public void checkServer(Server server) {
        // 1. Check server health
        ServerStatus newStatus = simulateServerCheck();
        String errorMessage = null;

        if (newStatus.isProblematic()) {
            errorMessage = generateErrorMessage(newStatus);
        }

        // 2. Update server status
        server.updateStatus(newStatus, errorMessage);

        // 3. Store server for Pull Model
        this.lastCheckedServer = server;

        // 4. Notify observers
        notifyObservers();
    }

    /**
     * Checks all servers in the monitoring list.
     */
    public void checkAllServers() {
        System.out.println("=== Starting server health check ===");

        for (Server server : servers) {
            checkServer(server);
        }

        System.out.println("=== Server health check completed ===\n");
    }

    /**
     * Adds an observer to receive server status notifications.
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
        System.out.println("Added observer: " + observer.getClass().getSimpleName());
    }

    /**
     * Removes an observer from receiving notifications.
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
        System.out.println("Removed observer: " + observer.getClass().getSimpleName());
    }

    /**
     * Notifies all observers.
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    /**
     * Simulates server health check.
     */
    private ServerStatus simulateServerCheck() {
        int rand = random.nextInt(100);
        if (rand < 60) return ServerStatus.ONLINE;
        else if (rand < 80) return ServerStatus.ERROR;
        else if (rand < 95) return ServerStatus.OFFLINE;
        else return ServerStatus.CRITICAL;
    }

    /**
     * Generates error message for problematic statuses.
     */
    private String generateErrorMessage(ServerStatus status) {
        return switch (status) {
            case ERROR -> "High response time detected";
            case CRITICAL -> "Service completely unresponsive";
            case OFFLINE -> "Server not reachable";
            default -> null;
        };
    }

    /**
     * Gets the server that was last checked.
     * Used by observers in Pull Model to get information about the change.
     */
    public Server getLastCheckedServer() {
        return lastCheckedServer;
    }
}