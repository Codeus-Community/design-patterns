package org.codeus.design_patterns.observer.messy;

import org.codeus.design_patterns.observer.common.Server;

/**
 * Demo showing the messy code problem.
 * Shows how one server check triggers multiple side effects.
 */
public class MessyMonitoringDemo {

    public static void main(String[] args) {
        System.out.println("=== Messy Monitor Demo ===");
        System.out.println("Problem: ONE method does EVERYTHING!\n");

        // Create monitor and add server
        MessyServerMonitor monitor = new MessyServerMonitor();
        Server server = new Server("WebServer", "192.168.1.10", 80);
        monitor.addServer(server);
        System.out.println();

        // Watch what happens with one simple check
        System.out.println(">>> Single server check triggers everything: <<<");
        monitor.checkServer(server);
        System.out.println();

        // Show what happens when we disable email
        System.out.println(">>> Disable email and check again: <<<");
        monitor.enableEmail(false);
        monitor.checkServer(server);
        System.out.println();

        // The core problem
        demonstrateProblem();
    }

    private static void demonstrateProblem() {
        System.out.println("=== THE PROBLEM ===");
        System.out.println("1. checkServer() method does 4 different jobs:");
        System.out.println("   - Health checking");
        System.out.println("   - Console logging");
        System.out.println("   - Email notifications");
        System.out.println("   - Slack notifications");
        System.out.println();

        System.out.println("2. What if business wants Discord notifications?");
        System.out.println("   - Modify checkServer() method");
        System.out.println("   - Add discordEnabled boolean");
        System.out.println("   - Risk breaking existing code!");
        System.out.println();

        System.out.println("3. Testing nightmare:");
        System.out.println("   - Can't test email logic without console output");
        System.out.println("   - Can't test health checking without notifications");
        System.out.println();

        System.out.println("SOLUTION: Observer Pattern!");
        System.out.println("- Separate each notification into its own Observer");
        System.out.println("- Easy to add/remove notification types");
        System.out.println("- Test each Observer independently");
    }
}