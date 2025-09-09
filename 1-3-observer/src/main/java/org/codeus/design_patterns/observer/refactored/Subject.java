package org.codeus.design_patterns.observer.refactored;

/**
 * Subject interface following "Gang of Four" specification with core Observer Pattern methods.
 * Generic interface for managing observers and notifications in any Observer implementation.
 */
public interface Subject {

    /**
     * Adds an observer to receive notifications about state changes.
     */
    void addObserver(Observer observer);

    /**
     * Removes an observer from receiving notifications.
     */
    void removeObserver(Observer observer);

    /**
     * Notifies all registered observers about state changes.
     * In the Pull Model, this method calls update(this) on each observer,
     * allowing observers to query the subject for the information they need.
     */
    void notifyObservers();
}