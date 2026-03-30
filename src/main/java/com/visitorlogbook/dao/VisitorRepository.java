package com.visitorlogbook.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visitorlogbook.model.VisitorRequest;

public interface VisitorRepository extends JpaRepository<VisitorRequest, Long> {
    List<VisitorRequest> findByHostEmail(String email);
}