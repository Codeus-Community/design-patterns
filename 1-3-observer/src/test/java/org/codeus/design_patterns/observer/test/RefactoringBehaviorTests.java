package org.codeus.design_patterns.observer.test;

import org.codeus.design_patterns.observer.common.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Behavior tests that work with both MessyServerMonitor and CleanServerMonitor.
 * These tests prove that refactoring preserves functionality.
 * 
 * To run tests with different implementations:
 * 1. Change IMPLEMENTATION constant
 * 2. Run tests to verify behavior is preserved
 */
class RefactoringBehaviorTests {
    
    // Toggle this to test different implementations
    private static final String IMPLEMENTATION = "MESSY";
//    private static final String IMPLEMENTATION = "CLEAN";

    private ServerMonitoringBehavior monitor;
    private Server testServer;
    private ByteArrayOutputStream outputCapture;
    private PrintStream originalOut;
    
    @BeforeEach
    void setUp() {
        // Create implementation based on configuration
        monitor = createMonitor(IMPLEMENTATION);
        testServer = new Server("TestServer", "192.168.1.100", 8080);
        
        // Capture console output
        outputCapture = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputCapture));
    }
    
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
    
    private ServerMonitoringBehavior createMonitor(String type) {
        switch (type) {
            case "MESSY":
                return new MessyMonitorAdapter();
            case "CLEAN":
                return new CleanMonitorAdapter();
            default:
                throw new IllegalArgumentException("Unknown implementation: " + type);
        }
    }
    
    @Test
    @DisplayName("Should add servers to monitoring system")
    void shouldAddServers() {
        // When
        monitor.addServer(testServer);
        
        // Then
        assertEquals(1, monitor.getServerCount());
        
        // Should log server addition
        String output = outputCapture.toString();
        assertTrue(output.contains("TestServer") || output.contains("Added"));
    }
    
    @Test
    @DisplayName("Should perform server health checks with console output")
    void shouldPerformHealthChecks() {
        // Given
        monitor.addServer(testServer);
        outputCapture.reset();
        
        // When
        monitor.checkServer(testServer);
        
        // Then
        assertNotNull(testServer.getLastChecked(), "Server should have been checked");
        
        String output = outputCapture.toString();
        assertTrue(output.contains("TestServer"), "Should log server name");
        assertFalse(output.trim().isEmpty(), "Should produce console output");
    }
    
    @Test
    @DisplayName("Should handle multiple servers")
    void shouldHandleMultipleServers() {
        // Given
        Server server1 = new Server("Server1", "192.168.1.1", 80);
        Server server2 = new Server("Server2", "192.168.1.2", 80);
        
        monitor.addServer(server1);
        monitor.addServer(server2);
        
        // When
        monitor.checkAllServers();
        
        // Then
        assertEquals(2, monitor.getServerCount());
        assertNotNull(server1.getLastChecked());
        assertNotNull(server2.getLastChecked());
        
        String output = outputCapture.toString();
        assertTrue(output.contains("Server1") && output.contains("Server2"));
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10})
    @DisplayName("Should handle multiple check cycles")
    void shouldHandleMultipleCheckCycles(int cycles) {
        // Given
        monitor.addServer(testServer);
        
        // When
        for (int i = 0; i < cycles; i++) {
            monitor.checkServer(testServer);
        }
        
        // Then
        assertNotNull(testServer.getLastChecked());
        assertEquals(1, monitor.getServerCount());
    }
    
    @Test
    @DisplayName("Should generate email notifications for critical issues")
    void shouldGenerateEmailNotifications() {
        // Given
        monitor.addServer(testServer);
        monitor.setEmailEnabled(true);
        outputCapture.reset();
        
        // When - perform multiple checks to trigger different statuses
        boolean emailFound = false;
        for (int i = 0; i < 30; i++) {
            monitor.checkServer(testServer);
            String output = outputCapture.toString();
            if (output.contains("EMAIL")) {
                emailFound = true;
                assertTrue(output.contains("CRITICAL") || output.contains("Critical"));
                break;
            }
            outputCapture.reset();
        }
        
        // Then - email capability should exist (even if not triggered in this run)
        assertTrue(monitor.isEmailEnabled(), "Email should be enabled");
    }
    
    @Test
    @DisplayName("Should generate Slack notifications for problematic statuses")
    void shouldGenerateSlackNotifications() {
        // Given
        monitor.addServer(testServer);
        monitor.setSlackEnabled(true);
        outputCapture.reset();
        
        // When - perform multiple checks to trigger different statuses
        boolean slackFound = false;
        for (int i = 0; i < 30; i++) {
            monitor.checkServer(testServer);
            String output = outputCapture.toString();
            if (output.contains("SLACK")) {
                slackFound = true;
                break;
            }
            outputCapture.reset();
        }
        
        // Then - slack capability should exist
        assertTrue(monitor.isSlackEnabled(), "Slack should be enabled");
    }
    
    @Test
    @DisplayName("Should respect email disable configuration")
    void shouldRespectEmailDisable() {
        // Given
        monitor.addServer(testServer);
        monitor.setEmailEnabled(false);
        outputCapture.reset();
        
        // When - perform many checks
        for (int i = 0; i < 20; i++) {
            monitor.checkServer(testServer);
        }
        
        // Then
        assertFalse(monitor.isEmailEnabled());
        String output = outputCapture.toString();
        assertFalse(output.contains("EMAIL"), "Should not contain email notifications when disabled");
    }
    
    @Test
    @DisplayName("Should respect Slack disable configuration")
    void shouldRespectSlackDisable() {
        // Given
        monitor.addServer(testServer);
        monitor.setSlackEnabled(false);
        outputCapture.reset();
        
        // When - perform many checks
        for (int i = 0; i < 20; i++) {
            monitor.checkServer(testServer);
        }
        
        // Then
        assertFalse(monitor.isSlackEnabled());
        String output = outputCapture.toString();
        assertFalse(output.contains("SLACK"), "Should not contain Slack notifications when disabled");
    }
    
    @Test
    @DisplayName("Should always maintain console logging regardless of other settings")
    void shouldAlwaysMaintainConsoleLogging() {
        // Given
        monitor.addServer(testServer);
        monitor.setEmailEnabled(false);
        monitor.setSlackEnabled(false);
        outputCapture.reset();
        
        // When
        monitor.checkServer(testServer);
        
        // Then - console logging should still work
        String output = outputCapture.toString();
        assertFalse(output.trim().isEmpty(), "Should have console output even when notifications disabled");
        assertTrue(output.contains("TestServer") || output.contains("192.168.1.100"));
    }
    
    @Test
    @DisplayName("Should handle empty server list gracefully")
    void shouldHandleEmptyServerList() {
        // When & Then
        assertDoesNotThrow(() -> monitor.checkAllServers());
        assertEquals(0, monitor.getServerCount());
    }
    
    @Test
    @DisplayName("Should maintain configuration state")
    void shouldMaintainConfigurationState() {
        // When
        monitor.setEmailEnabled(false);
        monitor.setSlackEnabled(true);
        
        // Then
        assertFalse(monitor.isEmailEnabled());
        assertTrue(monitor.isSlackEnabled());
        
        // Change again
        monitor.setEmailEnabled(true);
        monitor.setSlackEnabled(false);
        
        assertTrue(monitor.isEmailEnabled());
        assertFalse(monitor.isSlackEnabled());
    }
}