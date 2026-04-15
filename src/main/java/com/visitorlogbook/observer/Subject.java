package com.visitorlogbook.observer;

/**
 * Subject Interface - Observer Pattern
 * Defines methods for managing and notifying observers.
 */
public interface Subject {
    /**
     * Register an observer to receive notifications.
     */
    void attach(Observer observer);

    /**
     * Remove an observer from notifications.
     */
    void detach(Observer observer);

    /**
     * Notify all attached observers about an event.
     */
    void notifyObservers(String event);
}
