package org.codeus.design_patterns.observer.refactored;

/**
 * Observer interface using classic Pull Model where Subject passes itself via update(Subject).
 * This allows different Observers to extract different data as needed.
 */
public interface Observer {

    /**
     * Called when the Subject's state changes.
     * The observer receives a reference to the subject and can query it for needed information.
     */
    void update(Subject subject);
}