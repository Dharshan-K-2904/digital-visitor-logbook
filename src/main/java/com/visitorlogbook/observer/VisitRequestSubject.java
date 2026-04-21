package com.visitorlogbook.observer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * VisitRequestSubject – Observer Pattern: Concrete Subject.
 *
 * Holds the list of registered {@link Observer}s and fires events
 * whenever a visit-request status changes (approved, rejected, checked-in,
 * checked-out). Controllers / services call {@link #notifyObservers(String)}
 * after every state transition.
 *
 * Design Patterns:
 * - Observer : concrete Subject that manages and notifies its Observer list.
 *
 * Design Principles:
 * - Single Responsibility : only manages observers and event dispatch.
 * - Open/Closed : add new Observers without touching this class.
 * - Dependency Inversion : depends on {@link Observer} abstraction, not
 * concrete implementations.
 */
@Component
public class VisitRequestSubject implements Subject {

    /** Thread-safe observer list. */
    private final List<Observer> observers = Collections.synchronizedList(new ArrayList<>());

    // ── Subject contract ──────────────────────────────────────────────────────

    @Override
    public void attach(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Broadcast {@code event} to every registered observer.
     * Observers are notified in registration order.
     *
     * @param event a domain event string such as
     *              {@code "REQUEST_APPROVED"}, {@code "REQUEST_REJECTED"},
     *              {@code "CHECKED_IN"}, or {@code "CHECKED_OUT"}.
     */
    @Override
    public void notifyObservers(String event) {
        for (Observer observer : observers) {
            try {
                observer.update(event);
            } catch (Exception ex) {
                // One failing observer must not block the others.
                System.err.println("[VisitRequestSubject] Observer threw: " + ex.getMessage());
            }
        }
    }

    /**
     * Returns an unmodifiable view of the current observer list (for diagnostics).
     */
    public List<Observer> getObservers() {
        return Collections.unmodifiableList(observers);
    }
}
