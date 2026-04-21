package com.visitorlogbook.observer;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * NotificationObserver – Observer Pattern: Concrete Observer.
 *
 * Reacts to visit-request domain events fired by {@link VisitRequestSubject}.
 * In a production system this class would integrate with an email / push
 * notification service; here it logs structured messages to the console so
 * the pattern is clearly observable during demo runs.
 *
 * To add a new notification channel (e.g. SMS) implement {@link Observer}
 * and register it via {@link VisitRequestSubject#attach(Observer)} —
 * this class never needs to change (Open/Closed Principle).
 *
 * Design Patterns:
 *  - Observer : concrete Observer receiving domain events.
 *
 * Design Principles:
 *  - Single Responsibility : only responsible for notification delivery.
 *  - Open/Closed           : extend via new Observers; this class is closed.
 */
@Component
public class NotificationObserver implements Observer {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Domain events this observer understands. */
    public static final String REQUEST_SUBMITTED = "REQUEST_SUBMITTED";
    public static final String REQUEST_APPROVED  = "REQUEST_APPROVED";
    public static final String REQUEST_REJECTED  = "REQUEST_REJECTED";
    public static final String CHECKED_IN        = "CHECKED_IN";
    public static final String CHECKED_OUT       = "CHECKED_OUT";

    // ── Observer contract ─────────────────────────────────────────────────────

    /**
     * Handle a domain event published by the Subject.
     *
     * @param event the event constant (see static fields above)
     */
    @Override
    public void update(String event) {
        if (event == null || event.isBlank()) return;

        switch (event) {
            case REQUEST_SUBMITTED -> notify("SUBMITTED",
                    "A new visit request has been submitted and is awaiting host approval.");
            case REQUEST_APPROVED  -> notify("APPROVAL",
                    "Visit request APPROVED. The visitor may now check in at reception.");
            case REQUEST_REJECTED  -> notify("REJECTION",
                    "Visit request REJECTED. The visitor has been informed.");
            case CHECKED_IN        -> notify("CHECK-IN",
                    "Visitor has checked IN. Entry time recorded.");
            case CHECKED_OUT       -> notify("CHECK-OUT",
                    "Visitor has checked OUT. Duration and exit time recorded.");
            default                -> notify("EVENT",
                    "Unrecognised event received: " + event);
        }
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private void notify(String type, String message) {
        String timestamp = LocalDateTime.now().format(FMT);
        System.out.printf("[NOTIFICATION] [%s] [%s] %s%n", timestamp, type, message);
    }
}
