package com.visitorlogbook.model;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;         // ADMIN | HOST | SECURITY | VISITOR
    private boolean active;
    private LocalDateTime createdAt;

    public User() {}
    public Long getId()                               { return id; }
    public void setId(Long id)                        { this.id = id; }
    public String getName()                           { return name; }
    public void setName(String name)                  { this.name = name; }
    public String getEmail()                          { return email; }
    public void setEmail(String email)                { this.email = email; }
    public String getPassword()                       { return password; }
    public void setPassword(String password)          { this.password = password; }
    public String getRole()                           { return role; }
    public void setRole(String role)                  { this.role = role; }
    public boolean isActive()                         { return active; }
    public void setActive(boolean active)             { this.active = active; }
    public LocalDateTime getCreatedAt()               { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
package com.visitorlogbook.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String role;

    public User() {}

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
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
}