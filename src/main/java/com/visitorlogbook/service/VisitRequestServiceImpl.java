package com.visitorlogbook.service;

import com.visitorlogbook.dao.VisitRequestDAO;
import com.visitorlogbook.model.VisitRequest;
import com.visitorlogbook.observer.NotificationObserver;
import com.visitorlogbook.observer.VisitRequestSubject;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * VisitRequestServiceImpl – Service-layer implementation.
 *
 * Acts as the single orchestration point for all visit-request business
 * logic: it calls the DAO for persistence and fires Observer events for
 * cross-cutting notification concerns.
 *
 * Controllers are wired to the {@link VisitRequestService} interface (DIP),
 * not to this concrete class.
 *
 * Design Patterns applied:
 *  - DAO Pattern    : delegates all DB access to {@link VisitRequestDAO}.
 *  - Observer       : fires domain events via {@link VisitRequestSubject}.
 *
 * Design Principles:
 *  - Single Responsibility : owns visit-request business logic only.
 *  - Separation of Concerns: persistence = DAO; notifications = Observer.
 *  - Dependency Inversion  : depends on the DAO interface, not its impl.
 */
@Service
public class VisitRequestServiceImpl implements VisitRequestService {

    private final VisitRequestDAO       visitDAO;
    private final VisitRequestSubject   subject;

    /**
     * Constructor injection – both collaborators are injected as abstractions.
     * The Spring container resolves the concrete implementation at runtime.
     */
    public VisitRequestServiceImpl(VisitRequestDAO visitDAO,
                                   VisitRequestSubject subject,
                                   NotificationObserver notificationObserver) {
        this.visitDAO = visitDAO;
        this.subject  = subject;
        // Register the default notification observer at startup.
        this.subject.attach(notificationObserver);
    }

    // ── Business operations ───────────────────────────────────────────────────

    @Override
    public void submit(VisitRequest req) {
        visitDAO.save(req);
        subject.notifyObservers(NotificationObserver.REQUEST_SUBMITTED);
    }

    @Override
    public void approve(Long id) {
        visitDAO.updateStatus(id, "APPROVED", null);
        subject.notifyObservers(NotificationObserver.REQUEST_APPROVED);
    }

    @Override
    public void reject(Long id, String reason) {
        visitDAO.updateStatus(id, "REJECTED", reason);
        subject.notifyObservers(NotificationObserver.REQUEST_REJECTED);
    }

    @Override
    public boolean checkIn(Long id) {
        VisitRequest r = visitDAO.findById(id);
        if (r == null || !"APPROVED".equals(r.getStatus())) {
            return false;
        }
        visitDAO.updateCheckIn(id);
        subject.notifyObservers(NotificationObserver.CHECKED_IN);
        return true;
    }

    @Override
    public boolean checkOut(Long id) {
        VisitRequest r = visitDAO.findById(id);
        if (r == null || !"CHECKED_IN".equals(r.getStatus())) {
            return false;
        }
        visitDAO.updateCheckOut(id);
        subject.notifyObservers(NotificationObserver.CHECKED_OUT);
        return true;
    }

    // ── Read-only delegations ─────────────────────────────────────────────────

    @Override public VisitRequest findById(Long id)                         { return visitDAO.findById(id); }
    @Override public List<VisitRequest> findAll()                           { return visitDAO.findAll(); }
    @Override public List<VisitRequest> findByVisitorId(Long visitorId)     { return visitDAO.findByVisitorId(visitorId); }
    @Override public List<VisitRequest> findByHostId(Long hostId)           { return visitDAO.findByHostId(hostId); }
    @Override public List<VisitRequest> findByStatus(String status)         { return visitDAO.findByStatus(status); }
    @Override public List<VisitRequest> findFiltered(LocalDate from, LocalDate to, String status) {
        return visitDAO.findFiltered(from, to, status);
    }
    @Override public List<VisitRequest> findRecent(int limit)               { return visitDAO.findRecent(limit); }
    @Override public long countByStatus(String status)                      { return visitDAO.countByStatus(status); }
    @Override public long countByDate(LocalDate date)                       { return visitDAO.countByDate(date); }
}
