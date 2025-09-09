package org.codeus.design_patterns.observer.test;

import org.codeus.design_patterns.observer.messy.MessyServerMonitor;
import org.codeus.design_patterns.observer.common.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests demonstrating testing problems in messy code.
 * 
 * These tests show why the MessyServerMonitor approach is hard to test:
 * - Cannot test individual features in isolation
 * - Multiple side effects from single method calls
 * - Configuration and business logic mixed together
 * - Unpredictable behavior due to random status generation
 */
class MessyServerMonitorProblemsTest {
    
    private MessyServerMonitor monitor;
    private Server testServer;
    private ByteArrayOutputStream outputCapture;
    private PrintStream originalOut;
    
    @BeforeEach
    void setUp() {
        monitor = new MessyServerMonitor();
        testServer = new Server("TestServer", "127.0.0.1", 80);
        
        // Capture console output for testing
        outputCapture = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputCapture));
    }
    
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
    
    @Test
    @DisplayName("Should add server successfully")
    void shouldAddServer() {
        // When
        monitor.addServer(testServer);
        
        // Then
        assertEquals(1, monitor.getServers().size());
        assertTrue(monitor.getServers().contains(testServer));
        
        String consoleOutput = outputCapture.toString();
        assertTrue(consoleOutput.contains("Added server: TestServer"));
    }
    
    @Test
    @DisplayName("Problem: Cannot test individual notification types in isolation")
    void problemCannotTestIndividualNotificationTypes() {
        // This test documents the fundamental problem with the messy approach:
        // We CANNOT test just email logic without involving everything else
        
        monitor.addServer(testServer);
        outputCapture.reset();
        
        // To test email functionality properly, we would need to:
        // 1. Force the status to be CRITICAL (can't do - random generator)
        // 2. Ensure only email fires (can't do - console logging always fires)
        // 3. Verify email content without console noise (difficult)
        // 4. Test email without side effects from other integrations (impossible)
        
        // We can only test the entire checkServer() method as a black box
        monitor.checkServer(testServer);
        
        String output = outputCapture.toString();
        
        // We know SOMETHING happened, but can't isolate what
        assertFalse(output.trim().isEmpty(), "Should have some output");
        assertTrue(output.contains("TestServer"), "Should mention server name");
        
        // Problem: Can't verify specific email behavior without side effects
        assertTrue(true, "This test documents why isolated testing is impossible with messy code");
    }
    
    @Test
    @DisplayName("Problem: Unpredictable side effects due to random status generation")
    void problemUnpredictableSideEffects() {
        // Given
        monitor.addServer(testServer);
        outputCapture.reset();
        
        // When
        monitor.checkServer(testServer);
        
        // Then
        String consoleOutput = outputCapture.toString();
        
        // We can only test that SOMETHING happened
        assertTrue(consoleOutput.contains("TestServer"));
        assertNotNull(testServer.getLastChecked());
        
        // Problem: We can't predict which notifications will fire
        // The random status generator might produce any status
        // This makes the test non-deterministic
        assertTrue(consoleOutput.length() > 10, 
                  "Should have output, but we can't predict what exactly");
    }
    
    @Test
    @DisplayName("Problem: Configuration management becomes complex")
    void problemConfigurationManagementComplexity() {
        // Show how configuration becomes complex with multiple boolean flags
        
        // Initial state
        assertTrue(monitor.isEmailEnabled(), "Email should be enabled by default");
        assertTrue(monitor.isSlackEnabled(), "Slack should be enabled by default");
        
        // Changing configuration requires multiple method calls
        monitor.enableEmail(false);
        monitor.enableSlack(true);
        
        // Verify changes
        assertFalse(monitor.isEmailEnabled());
        assertTrue(monitor.isSlackEnabled());
        
        // Problem: This configuration approach doesn't scale
        // - Adding Discord would require: enableDiscord(), getDiscordEnabled()
        // - No way to configure per-server or per-status-type
        // - Static configuration that's hard to change dynamically
        
        // Imagine having 10 different notification types...
        assertTrue(true, "Configuration with boolean flags doesn't scale");
    }
    
    @Test
    @DisplayName("Problem: Testing email configuration is unreliable")
    void problemTestingEmailConfigurationIsUnreliable() {
        // Given
        monitor.addServer(testServer);
        monitor.enableEmail(false);
        outputCapture.reset();
        
        // When - try multiple times to see if email logic works
        boolean emailFoundWhenDisabled = false;
        for (int i = 0; i < 20; i++) {
            monitor.checkServer(testServer);
            String output = outputCapture.toString();
            if (output.contains("EMAIL:")) {
                emailFoundWhenDisabled = true;
                break;
            }
            outputCapture.reset();
        }
        
        // Then
        assertFalse(emailFoundWhenDisabled, "Should not send emails when disabled");
        
        // Problem with this test:
        // 1. We can't force a CRITICAL status to properly test email logic
        // 2. The test might pass even if email logic is broken (if no CRITICAL status occurs)
        // 3. We're testing configuration AND business logic together
        // 4. Test is non-deterministic due to random status generation
    }
    
    @Test
    @DisplayName("Problem: Cannot test business logic without infrastructure concerns")
    void problemCannotTestBusinessLogicWithoutInfrastructure() {
        // Given
        monitor.addServer(testServer);
        outputCapture.reset();
        
        // When
        monitor.checkServer(testServer);
        
        // Problem: To test the business rule "email only for CRITICAL status",
        // we need to:
        // 1. Deal with random status generation (infrastructure)
        // 2. Parse console output (infrastructure)  
        // 3. Handle multiple notification types firing (infrastructure)
        
        // We can't test the pure business logic:
        // "if (status == CRITICAL) send email"
        
        String output = outputCapture.toString();
        
        // All we can do is verify that the method executed
        assertTrue(output.length() > 0, "Method executed, but business logic testing is mixed with infrastructure");
        
        // In clean code, we could test: emailObserver.update(criticalEvent)
        // and verify email sending without any infrastructure concerns
    }
    
    @Test
    @DisplayName("Problem: Extension requires modifying existing code")
    void problemExtensionRequiresModifyingExistingCode() {
        // This test documents what would be required to add Discord notifications:
        
        // Required changes to MessyServerMonitor:
        // 1. Add discordEnabled field
        // 2. Add Discord notification logic to checkServer() method  
        // 3. Add enableDiscord(boolean) method
        // 4. Add getDiscordEnabled() getter method
        // 5. Update all tests that check console output
        // 6. Risk breaking existing email/slack functionality
        
        // That's 6+ places to modify for ONE new feature!
        // High chance of introducing bugs in existing functionality
        
        monitor.addServer(testServer);
        monitor.checkServer(testServer);
        
        // Current approach violates Open/Closed Principle
        // Classes should be open for extension, closed for modification
        assertTrue(true, "Adding new notification types requires modifying existing code - violates Open/Closed Principle");
    }
    
    @Test
    @DisplayName("Basic functionality works but testing is problematic")
    void basicFunctionalityWorksButTestingIsProblematic() {
        // Given
        Server server1 = new Server("Server1", "192.168.1.1", 80);
        Server server2 = new Server("Server2", "192.168.1.2", 80);
        
        monitor.addServer(server1);
        monitor.addServer(server2);
        outputCapture.reset();
        
        // When
        monitor.checkAllServers();
        
        // Then - basic functionality works
        assertEquals(2, monitor.getServers().size());
        assertNotNull(server1.getLastChecked());
        assertNotNull(server2.getLastChecked());
        
        String output = outputCapture.toString();
        assertTrue(output.contains("Server1") && output.contains("Server2"));
        
        // But testing is problematic:
        // - Can't control what notifications fire
        // - Can't test notifications in isolation
        // - Output format is mixed and hard to parse
        // - No way to mock or stub individual components
        
        // This test passes but doesn't give us confidence in the system
        assertTrue(true, "Basic functionality works, but robust testing is nearly impossible");
    }
}