package com.visitorlogbook.observer;

/**
 * Observer Interface - Observer Pattern
 * Defines the contract for objects that want to be notified of events.
 */
public interface Observer {
    /**
     * Update method called when observed subject changes.
     *
     * @param event the event that occurred
     */
    void update(String event);
}
