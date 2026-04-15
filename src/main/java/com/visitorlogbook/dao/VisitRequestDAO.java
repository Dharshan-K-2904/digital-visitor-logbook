package com.visitorlogbook.dao;

import com.visitorlogbook.model.VisitRequest;
import java.time.LocalDate;
import java.util.List;

public interface VisitRequestDAO {
    List<VisitRequest> findAll();
    VisitRequest findById(Long id);
    List<VisitRequest> findByVisitorId(Long visitorId);
    List<VisitRequest> findByHostId(Long hostId);
    List<VisitRequest> findByStatus(String status);
    List<VisitRequest> findFiltered(LocalDate from, LocalDate to, String status);
    List<VisitRequest> findRecent(int limit);
    void save(VisitRequest req);
    void updateStatus(Long id, String status, String rejectionReason);
    void updateCheckIn(Long id);
    void updateCheckOut(Long id);
    long countByStatus(String status);
    long countByDate(LocalDate date);
}
