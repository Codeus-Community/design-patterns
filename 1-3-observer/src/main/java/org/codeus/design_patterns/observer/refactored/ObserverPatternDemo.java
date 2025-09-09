package org.codeus.design_patterns.observer.refactored;

import org.codeus.design_patterns.observer.common.Server;

/**
 * Demonstration of the Observer Pattern implementation.
 *
 * This demo shows the benefits of the refactored clean code using Observer Pattern
 * compared to the MessyServerMonitor approach. It demonstrates:
 *
 * 1. Clean separation of concerns
 * 2. Easy extensibility
 * 3. Observer registration and removal
 */
public class ObserverPatternDemo {

    public static void main(String[] args) {
        System.out.println("=== Observer Pattern Server Monitoring Demo ===");
        System.out.println("This demo shows the clean, extensible solution using Observer Pattern\n");

        // 1. Create the clean monitoring system (Subject)
        CleanServerMonitor monitor = new CleanServerMonitor();

        // 2. Create different types of observers
        ConsoleLoggingObserver consoleObserver = new ConsoleLoggingObserver();
        EmailNotificationObserver emailObserver = new EmailNotificationObserver("admin@company.com");
        SlackNotificationObserver slackObserver = new SlackNotificationObserver("#devops-alerts");

        // 3. Register observers with the Subject
        System.out.println(">>> Registering Observers <<<");
        monitor.addObserver(consoleObserver);
        monitor.addObserver(emailObserver);
        monitor.addObserver(slackObserver);
        System.out.println();

        // 4. Add servers to monitor
        System.out.println(">>> Adding Servers to Monitor <<<");
        Server webServer = new Server("Web Server", "192.168.1.10", 80);
        Server dbServer = new Server("Database Server", "192.168.1.20", 5432);
        Server apiServer = new Server("API Server", "192.168.1.30", 8080);

        monitor.addServer(webServer);
        monitor.addServer(dbServer);
        monitor.addServer(apiServer);
        System.out.println();

        // 5. Demonstrate clean monitoring with multiple observers
        System.out.println(">>> Server Health Checks (All Observers Active) <<<");
        System.out.println("Watch how one server check triggers multiple observers cleanly:");
        System.out.println();

        for (int i = 1; i <= 3; i++) {
            System.out.println("--- Check Cycle " + i + " ---");
            monitor.checkServer(webServer);
            System.out.println();

            // Add delay for readability
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // 6. Demonstrate easy extensibility by adding a new observer dynamically
        System.out.println(">>> Easy Extensibility <<<");
        System.out.println("Adding a new observer type at runtime:");

        // Create a simple custom observer
        Observer customObserver = new Observer() {
            @Override
            public void update(Subject subject) {
                if (subject instanceof CleanServerMonitor) {
                    System.out.println("CUSTOM: Analytics logged for server check");
                }
            }
        };

        monitor.addObserver(customObserver);
        System.out.println();

        System.out.println("--- Server Check with New Observer Added ---");
        monitor.checkServer(apiServer);
        System.out.println();

        // 8. Demonstrate observer removal
        System.out.println(">>> Observer Removal <<<");
        System.out.println("Removing email observer:");
        monitor.removeObserver(emailObserver);
        System.out.println();

        System.out.println("--- Final Server Check (No Email) ---");
        monitor.checkServer(webServer);
        System.out.println();

        // 9. Compare with MessyServerMonitor problems
        showComparisonWithMessyApproach();
    }

    /**
     * Shows the benefits of Observer Pattern compared to MessyServerMonitor.
     */
    private static void showComparisonWithMessyApproach() {
        System.out.println("=== COMPARISON: Observer Pattern vs Messy Approach ===");
        System.out.println();

        System.out.println("MESSY APPROACH PROBLEMS:");
        System.out.println("- Single class doing everything (console, email, slack)");
        System.out.println("- Hard to test individual features");
        System.out.println("- Boolean flags everywhere (emailEnabled, slackEnabled)");
        System.out.println("- To add Discord: modify checkServer() method + add config");
        System.out.println("- Tight coupling - changing email affects everything");
        System.out.println();

        System.out.println("OBSERVER PATTERN BENEFITS:");
        System.out.println("- Single Responsibility - each observer has one job");
        System.out.println("- Easy testing - test each observer independently");
        System.out.println("- Easy extensibility - new observer = new class, no modifications");
        System.out.println("- Loose coupling - observers don't know about each other");
        System.out.println("- Open/Closed Principle - open for extension, closed for modification");
        System.out.println();

        System.out.println("ADDING NEW OBSERVER TYPE:");
        System.out.println("Messy approach: Modify checkServer() method + add boolean flag");
        System.out.println("Observer approach: Create new Observer class + monitor.addObserver()");
        System.out.println();

        System.out.println("REAL-WORLD BENEFITS:");
        System.out.println("- Different teams can own different observers");
        System.out.println("- Easy integration with third-party services");
        System.out.println("- Environment-specific observer configurations");
    }
}