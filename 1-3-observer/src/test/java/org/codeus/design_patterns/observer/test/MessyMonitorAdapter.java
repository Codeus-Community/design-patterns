package org.codeus.design_patterns.observer.test;

import org.codeus.design_patterns.observer.messy.MessyServerMonitor;
import org.codeus.design_patterns.observer.common.Server;

/**
 * Adapter for MessyServerMonitor to work with common test interface.
 */
public class MessyMonitorAdapter implements ServerMonitoringBehavior {

    private final MessyServerMonitor monitor;

    public MessyMonitorAdapter() {
        this.monitor = new MessyServerMonitor();
    }

    @Override
    public void addServer(Server server) {
        monitor.addServer(server);
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
        monitor.enableEmail(enabled);
    }

    @Override
    public void setSlackEnabled(boolean enabled) {
        monitor.enableSlack(enabled);
    }

    @Override
    public int getServerCount() {
        return monitor.getServers().size();
    }

    @Override
    public boolean isEmailEnabled() {
        return monitor.isEmailEnabled();
    }

    @Override
    public boolean isSlackEnabled() {
        return monitor.isSlackEnabled();
    }
}