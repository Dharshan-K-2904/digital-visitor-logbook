package com.visitorlogbook.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.visitorlogbook.model.VisitorRequest;

public interface VisitorRepository extends JpaRepository<VisitorRequest, Long> {
    List<VisitorRequest> findByHostEmail(String email);
    
    // Search and filter methods for Member 3
    List<VisitorRequest> findByStatus(String status);
    
    List<VisitorRequest> findByVisitorNameContainingIgnoreCase(String name);
    
    @Query("SELECT v FROM VisitorRequest v WHERE v.status = 'APPROVED' AND v.checkInTime IS NULL")
    List<VisitorRequest> findApprovedAndNotCheckedIn();
    
    @Query("SELECT v FROM VisitorRequest v WHERE v.checkInTime IS NOT NULL AND v.checkOutTime IS NULL")
    List<VisitorRequest> findCheckedInVisitors();
    
    @Query("SELECT v FROM VisitorRequest v WHERE v.checkInTime BETWEEN :startDate AND :endDate")
    List<VisitorRequest> findByCheckInTimeBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT v FROM VisitorRequest v WHERE " +
           "(:status IS NULL OR v.status = :status) AND " +
           "(:searchTerm IS NULL OR LOWER(v.visitorName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(v.hostEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<VisitorRequest> searchAndFilter(@Param("status") String status, @Param("searchTerm") String searchTerm);
}