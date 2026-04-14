package com.visitorlogbook.service;

import com.visitorlogbook.dao.VisitorRepository;
import com.visitorlogbook.model.VisitorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class VisitorService {

    @Autowired
    private VisitorRepository repo;

    public void createRequest(VisitorRequest request) {
        request.setStatus("PENDING");
        request.setRequestTime(LocalDateTime.now());
        repo.save(request);
    }

    public List<VisitorRequest> getRequests(String email) {
        return repo.findByHostEmail(email);
    }

    public void updateStatus(Long id, String status) {
        VisitorRequest req = repo.findById(id).get();
        req.setStatus(status);
        repo.save(req);
    }
    
    // ========== MEMBER 3: CHECK-IN AND CHECK-OUT METHODS ==========
    
    /**
     * Check-in a visitor (UC-04)
     * Security personnel verifies approval and logs entry time
     */
    public boolean checkIn(Long requestId) {
        Optional<VisitorRequest> optionalRequest = repo.findById(requestId);
        
        if (optionalRequest.isPresent()) {
            VisitorRequest request = optionalRequest.get();
            
            // Verify approval status
            if ("APPROVED".equals(request.getStatus()) && request.getCheckInTime() == null) {
                request.setCheckInTime(LocalDateTime.now());
                request.setStatus("CHECKED_IN");
                repo.save(request);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check-out a visitor (UC-05)
     * Security personnel logs exit time and calculates duration
     */
    public boolean checkOut(Long requestId) {
        Optional<VisitorRequest> optionalRequest = repo.findById(requestId);
        
        if (optionalRequest.isPresent()) {
            VisitorRequest request = optionalRequest.get();
            
            // Verify visitor is checked in
            if (request.getCheckInTime() != null && request.getCheckOutTime() == null) {
                LocalDateTime checkOutTime = LocalDateTime.now();
                request.setCheckOutTime(checkOutTime);
                
                // Calculate visit duration in minutes
                Duration duration = Duration.between(request.getCheckInTime(), checkOutTime);
                request.setVisitDuration((int) duration.toMinutes());
                
                request.setStatus("CHECKED_OUT");
                repo.save(request);
                return true;
            }
        }
        return false;
    }
    
    // ========== MEMBER 3: SEARCH AND FILTER METHODS ==========
    
    /**
     * Get visitors pending check-in (approved but not yet checked in)
     */
    public List<VisitorRequest> getPendingCheckIns() {
        return repo.findApprovedAndNotCheckedIn();
    }
    
    /**
     * Get currently checked-in visitors
     */
    public List<VisitorRequest> getCurrentlyCheckedIn() {
        return repo.findCheckedInVisitors();
    }
    
    /**
     * Search and filter visitors
     */
    public List<VisitorRequest> searchVisitors(String status, String searchTerm) {
        return repo.searchAndFilter(status, searchTerm);
    }
    
    /**
     * Get all visitors (for admin/security view)
     */
    public List<VisitorRequest> getAllVisitors() {
        return repo.findAll();
    }
    
    /**
     * Get visitor request by ID
     */
    public Optional<VisitorRequest> getRequestById(Long id) {
        return repo.findById(id);
    }
}