package com.visitorlogbook.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * User Entity - Represents a system user (Visitor, Host, Admin, Security).
 * 
 * Roles:
 * - VISITOR: Can request visits
 * - HOST: Can approve/reject visit requests
 * - SECURITY: Can check in/out visitors
 * - ADMIN: Can manage users and view reports
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // User's full name
    private String email;       // Unique email identifier
    private String password;    // Password (plain text in this demo; hash in production)
    private String role;        // User role: VISITOR, HOST, SECURITY, ADMIN
    private boolean active;     // Account status
    private LocalDateTime createdAt; // Account creation timestamp

    /**
     * Default constructor - initializes with default values.
     */
    public User() {
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Constructor with all parameters.
     */
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}