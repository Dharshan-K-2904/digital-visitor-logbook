package com.visitorlogbook.service;

import com.visitorlogbook.model.User;

import java.util.List;

/**
 * UserManagementService – abstraction for user CRUD and auth business logic.
 *
 * Follows the Dependency Inversion Principle: controllers depend on this
 * interface, not on any DAO or repository concrete class.
 *
 * Design Principles:
 *  - Single Responsibility  : owns only user-management concerns.
 *  - Dependency Inversion   : high-level modules depend on this abstraction.
 *  - Interface Segregation  : only user-related operations are declared here.
 */
public interface UserManagementService {

    /** Authenticate by email + password.  Returns null on failure. */
    User authenticate(String email, String password);

    /** Register a new user; returns the saved user or null on duplicate email. */
    User register(User user);

    List<User> findAll();
    User findById(Long id);
    User findByEmail(String email);
    List<User> findByRole(String role);
    void save(User user);
    void update(User user);
    void delete(Long id);
    void toggleStatus(Long id);
    long countByRole(String role);
}
