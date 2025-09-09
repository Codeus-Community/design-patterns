package org.codeus.design_patterns.observer.test;

import org.codeus.design_patterns.observer.common.Server;
import org.codeus.design_patterns.observer.refactored.CleanServerMonitor;
import org.codeus.design_patterns.observer.refactored.ConsoleLoggingObserver;
import org.codeus.design_patterns.observer.refactored.EmailNotificationObserver;
import org.codeus.design_patterns.observer.refactored.SlackNotificationObserver;

/**
 * Adapter for CleanServerMonitor to work with common test interface.
 * 
 * Note: Since our refactored observers don't have enable/disable functionality,
 * we simulate this by adding/removing observers from the monitor.
 */
public class CleanMonitorAdapter implements ServerMonitoringBehavior {
    
    private final CleanServerMonitor monitor;
    private final ConsoleLoggingObserver consoleObserver;
    private final EmailNotificationObserver emailObserver;
    private final SlackNotificationObserver slackObserver;
    
    private boolean emailEnabled = true;
    private boolean slackEnabled = true;
    private int serverCount = 0;
    
    public CleanMonitorAdapter() {
        this.monitor = new CleanServerMonitor();
        this.consoleObserver = new ConsoleLoggingObserver();
        this.emailObserver = new EmailNotificationObserver("admin@company.com");
        this.slackObserver = new SlackNotificationObserver("#alerts");
        
        // Register all observers initially
        monitor.addObserver(consoleObserver);
        monitor.addObserver(emailObserver);
        monitor.addObserver(slackObserver);
    }
    
    @Override
    public void addServer(Server server) {
        monitor.addServer(server);
        serverCount++;
    }
    
    @Override
    public void checkServer(Server server) {
        monitor.checkServer(server);
    }
    
    @Override
    public void checkAllServers() {
        monitor.checkAllServers();
    }
    
    @Override
    public void setEmailEnabled(boolean enabled) {
        if (emailEnabled && !enabled) {
            // Disable: remove observer
            monitor.removeObserver(emailObserver);
        } else if (!emailEnabled && enabled) {
            // Enable: add observer back
            monitor.addObserver(emailObserver);
        }
        emailEnabled = enabled;
    }
    
    @Override
    public void setSlackEnabled(boolean enabled) {
        if (slackEnabled && !enabled) {
            // Disable: remove observer
            monitor.removeObserver(slackObserver);
        } else if (!slackEnabled && enabled) {
            // Enable: add observer back
            monitor.addObserver(slackObserver);
        }
        slackEnabled = enabled;
    }
    
    @Override
    public int getServerCount() {
        // Manual tracking since CleanServerMonitor doesn't expose server count
        return serverCount;
    }
    
    @Override
    public boolean isEmailEnabled() {
        return emailEnabled;
    }
    
    @Override
    public boolean isSlackEnabled() {
        return slackEnabled;
    }
}