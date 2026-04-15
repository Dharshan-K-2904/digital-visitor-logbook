package com.visitorlogbook.observer;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * NotificationObserver - Observer Pattern Implementation
 * Observes and reacts to visit request status changes.
 * Triggered on request approval and rejection events.
 */
@Component
public class NotificationObserver implements Observer {

    /**
     * Handle notification updates when visit request status changes.
     * This is called when a request is approved or rejected.
     *
     * @param event the event description (e.g., "REQUEST_APPROVED", "REQUEST_REJECTED")
     */
    @Override
    public void update(String event) {
        if (event == null || event.trim().isEmpty()) {
            return;
        }

        switch (event) {
            case "REQUEST_APPROVED" -> handleApprovalNotification();
            case "REQUEST_REJECTED" -> handleRejectionNotification();
            case "CHECKED_IN" -> handleCheckInNotification();
            case "CHECKED_OUT" -> handleCheckOutNotification();
            default -> logEvent(event);
        }
    }

    /**
     * Handle notification when a visit request is approved.
     */
    private void handleApprovalNotification() {
        String message = String.format("[%s] Visit request approved. Visitor can now check in.", 
                                      LocalDateTime.now());
        logNotification("APPROVAL", message);
    }

    /**
     * Handle notification when a visit request is rejected.
     */
    private void handleRejectionNotification() {
        String message = String.format("[%s] Visit request rejected. Visitor has been notified.", 
                                      LocalDateTime.now());
        logNotification("REJECTION", message);
    }

    /**
     * Handle notification when a visitor checks in.
     */
    private void handleCheckInNotification() {
        String message = String.format("[%s] Visitor has checked in.", LocalDateTime.now());
        logNotification("CHECK_IN", message);
    }

    /**
     * Handle notification when a visitor checks out.
     */
    private void handleCheckOutNotification() {
        String message = String.format("[%s] Visitor has checked out.", LocalDateTime.now());
        logNotification("CHECK_OUT", message);
    }

    /**
     * Log notification event.
     */
    private void logNotification(String type, String message) {
        System.out.println("[NOTIFICATION] [" + type + "] " + message);
    }

    /**
     * Log generic event.
     */
    private void logEvent(String event) {
        System.out.println("[EVENT] " + event + " at " + LocalDateTime.now());
    }
}
