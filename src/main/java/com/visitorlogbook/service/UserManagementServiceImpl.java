package com.visitorlogbook.service;

import com.visitorlogbook.dao.UserDAO;
import com.visitorlogbook.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserManagementServiceImpl – concrete implementation of {@link UserManagementService}.
 *
 * Encapsulates all user business logic and delegates persistence to
 * the {@link UserDAO} abstraction (DAO Pattern / DIP).
 *
 * Design Patterns applied:
 *  - DAO Pattern   : all DB access via {@link UserDAO} interface.
 *
 * Design Principles:
 *  - Single Responsibility : owns user business logic; nothing else.
 *  - Dependency Inversion  : depends on {@link UserDAO} interface.
 *  - Separation of Concerns: auth logic here; DB details in DAO impl.
 */
@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final UserDAO userDAO;

    public UserManagementServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // ── Auth ──────────────────────────────────────────────────────────────────

    /**
     * Authenticate a user by email and plain-text password.
     * Returns {@code null} if credentials are invalid or the account is disabled.
     *
     * NOTE: In production replace plain-text comparison with BCrypt.
     */
    @Override
    public User authenticate(String email, String password) {
        if (email == null || password == null) return null;
        User user = userDAO.findByEmail(email.trim());
        if (user == null || !user.isActive()) return null;
        return user.getPassword().equals(password) ? user : null;
    }

    /**
     * Register a new user.
     * Returns {@code null} if the email is already taken.
     */
    @Override
    public User register(User user) {
        if (user == null) return null;
        if (userDAO.findByEmail(user.getEmail()) != null) return null; // duplicate
        user.setActive(true);
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("VISITOR");
        }
        userDAO.save(user);
        return user;
    }

    // ── CRUD delegations ──────────────────────────────────────────────────────

    @Override public List<User> findAll()                        { return userDAO.findAll(); }
    @Override public User findById(Long id)                      { return userDAO.findById(id); }
    @Override public User findByEmail(String email)              { return userDAO.findByEmail(email); }
    @Override public List<User> findByRole(String role)          { return userDAO.findByRole(role); }
    @Override public void save(User user)                        { userDAO.save(user); }
    @Override public void update(User user)                      { userDAO.update(user); }
    @Override public void delete(Long id)                        { userDAO.delete(id); }
    @Override public void toggleStatus(Long id)                  { userDAO.toggleStatus(id); }
    @Override public long countByRole(String role)               { return userDAO.countByRole(role); }
}
