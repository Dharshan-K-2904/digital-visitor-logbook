package com.visitorlogbook.service;

import com.visitorlogbook.dao.VisitorRepository;
import com.visitorlogbook.model.VisitorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
}