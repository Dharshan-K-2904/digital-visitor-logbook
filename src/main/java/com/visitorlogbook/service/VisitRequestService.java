package com.visitorlogbook.service;

import com.visitorlogbook.model.VisitRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * VisitRequestService – Service-layer abstraction (Dependency Inversion Principle).
 *
 * Controllers depend on this interface, not on {@code VisitRequestDAOImpl}
 * or any other concrete class.  Swapping implementations (e.g. switching
 * from JDBC to JPA) requires zero changes in any controller.
 *
 * Design Principles:
 *  - Dependency Inversion  : high-level modules depend on this abstraction.
 *  - Open/Closed           : new behaviours added via new implementations.
 *  - Interface Segregation : only methods that belong to visit-request logic.
 */
public interface VisitRequestService {

    /** Submit a new visit request (sets PENDING status + timestamp). */
    void submit(VisitRequest req);

    /** Approve the request and fire the APPROVED observer event. */
    void approve(Long id);

    /** Reject the request with an optional reason and fire the REJECTED event. */
    void reject(Long id, String reason);

    /** Record visitor check-in and fire the CHECKED_IN event. */
    boolean checkIn(Long id);

    /** Record visitor check-out, calculate duration, and fire CHECKED_OUT event. */
    boolean checkOut(Long id);

    VisitRequest findById(Long id);
    List<VisitRequest> findAll();
    List<VisitRequest> findByVisitorId(Long visitorId);
    List<VisitRequest> findByHostId(Long hostId);
    List<VisitRequest> findByStatus(String status);
    List<VisitRequest> findFiltered(LocalDate from, LocalDate to, String status);
    List<VisitRequest> findRecent(int limit);
    long countByStatus(String status);
    long countByDate(LocalDate date);
}
