package com.visitorlogbook.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visitorlogbook.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}