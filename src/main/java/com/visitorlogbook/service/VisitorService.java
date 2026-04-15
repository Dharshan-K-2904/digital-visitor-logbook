package com.visitorlogbook.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.visitorlogbook.model.VisitorRequest;
import com.visitorlogbook.repository.VisitorRepository;

@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public void createRequest(VisitorRequest request) {
        request.setStatus("PENDING");
        request.setRequestTime(LocalDateTime.now());
        visitorRepository.save(request);
    }

    public List<VisitorRequest> getRequests(String email) {
        return visitorRepository.findByHostEmail(email);
    }

    public void updateStatus(Long id, String status) {
        if (id != null) {
            visitorRepository.findById(id).ifPresent(request -> {
                request.setStatus(status);
                visitorRepository.save(request);
            });
        }
    }

    public boolean checkIn(Long requestId) {
        if (requestId == null) {
            return false;
        }

        Optional<VisitorRequest> optional = visitorRepository.findById(requestId);

        if (optional.isEmpty()) {
            return false;
        }

        VisitorRequest request = optional.get();

        if ("APPROVED".equals(request.getStatus())
                && request.getCheckInTime() == null) {

            request.setCheckInTime(LocalDateTime.now());
            request.setStatus("CHECKED_IN");
            visitorRepository.save(request);
            return true;
        }

        return false;
    }

    public boolean checkOut(Long requestId) {
        if (requestId == null) {
            return false;
        }

        Optional<VisitorRequest> optional = visitorRepository.findById(requestId);

        if (optional.isEmpty()) {
            return false;
        }

        VisitorRequest request = optional.get();

        if (request.getCheckInTime() != null
                && request.getCheckOutTime() == null) {

            LocalDateTime checkOut = LocalDateTime.now();
            request.setCheckOutTime(checkOut);

            Duration duration = Duration.between(
                    request.getCheckInTime(),
                    checkOut
            );

            request.setVisitDuration((int) duration.toMinutes());
            request.setStatus("CHECKED_OUT");

            visitorRepository.save(request);
            return true;
        }

        return false;
    }

    public List<VisitorRequest> getPendingCheckIns() {
        return visitorRepository.findApprovedAndNotCheckedIn();
    }

    public List<VisitorRequest> getCurrentlyCheckedIn() {
        return visitorRepository.findCheckedInVisitors();
    }

    public List<VisitorRequest> searchVisitors(String status, String searchTerm) {
        return visitorRepository.searchAndFilter(status, searchTerm);
    }

    public List<VisitorRequest> getAllVisitors() {
        return visitorRepository.findAll();
    }

    public Optional<VisitorRequest> getRequestById(Long id) {
        if (id != null) {
            return visitorRepository.findById(id);
        }
        return Optional.empty();
    }
}