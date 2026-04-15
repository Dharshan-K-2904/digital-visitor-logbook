package com.visitorlogbook.factory;

import com.visitorlogbook.model.User;
import org.springframework.stereotype.Component;

/**
 * UserFactory - Factory Pattern Implementation
 * Centralizes user creation logic with consistent initialization.
 * Ensures all users are created with proper defaults and validation.
 */
@Component
public class UserFactory {

    /**
     * Create a new User with full details.
     */
    public User createUser(String name, String email, String password, String role) {
        validateInput(name, email, password, role);
        return new User(name, email, password, role);
    }

    /**
     * Create a visitor user with default VISITOR role.
     */
    public User createVisitor(String name, String email, String password) {
        return createUser(name, email, password, "VISITOR");
    }

    /**
     * Create a host user with HOST role.
     */
    public User createHost(String name, String email, String password) {
        return createUser(name, email, password, "HOST");
    }

    /**
     * Create a security staff user with SECURITY role.
     */
    public User createSecurityStaff(String name, String email, String password) {
        return createUser(name, email, password, "SECURITY");
    }

    /**
     * Create an admin user with ADMIN role.
     */
    public User createAdmin(String name, String email, String password) {
        return createUser(name, email, password, "ADMIN");
    }

    /**
     * Validate user input parameters.
     */
    private void validateInput(String name, String email, String password, String role) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be empty");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
        if (role == null || (!isValidRole(role))) {
            throw new IllegalArgumentException("Invalid user role");
        }
    }

    /**
     * Check if the role is valid.
     */
    private boolean isValidRole(String role) {
        return role.equals("VISITOR") || role.equals("HOST") || 
               role.equals("SECURITY") || role.equals("ADMIN");
    }
}
