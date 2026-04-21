package com.visitorlogbook.factory;

import com.visitorlogbook.model.User;

/**
 * UserFactory – Abstract Creator (GoF Factory Method Pattern).
 *
 * ┌─────────────────────────────────────────────────────────────────┐
 * │                   GoF Factory Method Pattern                    │
 * │                                                                 │
 * │   Creator (abstract)        Product                             │
 * │   ──────────────────        ───────                             │
 * │   UserFactory               User                                │
 * │     └── createUser()  ───►  new User(...)                       │
 * │                                                                 │
 * │   ConcreteCreator           (uses the product)                  │
 * │   ──────────────────                                            │
 * │   VisitorUserFactory   ──►  User(role=VISITOR)                  │
 * │   HostUserFactory      ──►  User(role=HOST)                     │
 * │   SecurityUserFactory  ──►  User(role=SECURITY)                 │
 * │   AdminUserFactory     ──►  User(role=ADMIN)                    │
 * └─────────────────────────────────────────────────────────────────┘
 *
 * This abstract class defines the <b>factory method</b> {@link #createUser}
 * that every concrete subclass must implement.  It also provides a
 * {@link #getUser} template method that performs shared pre/post steps
 * (validation, active-flag defaulting) around the factory method call.
 *
 * Callers use {@link UserFactoryRegistry} to obtain the correct concrete
 * factory for a given role at runtime — they never depend on a specific
 * subclass.
 *
 * Design Pattern:
 *  - Factory Method (GoF) : {@link #createUser} is the factory method.
 *
 * Design Principles:
 *  - Open/Closed           : new roles → add a subclass; this class is closed.
 *  - Single Responsibility : only responsible for the user-creation contract.
 *  - Dependency Inversion  : clients depend on this abstraction, not {@code new User()}.
 */
public abstract class UserFactory {

    // ── Factory Method (abstract) ─────────────────────────────────────────────

    /**
     * Factory Method – subclasses implement this to create role-specific Users.
     *
     * @param name     the user's full name
     * @param email    the user's unique email address
     * @param password the user's plain-text password (hash before storing in production)
     * @return a new, fully initialised {@link User} for the concrete role
     */
    protected abstract User createUser(String name, String email, String password);

    // ── Template Method (shared pre/post logic) ───────────────────────────────

    /**
     * Template Method – public entry point.
     *
     * Runs shared validation, calls the abstract {@link #createUser} factory
     * method, then applies common post-construction defaults (active flag).
     * Subclasses never need to duplicate this logic.
     *
     * @param name     the user's full name
     * @param email    the user's unique email address
     * @param password the user's plain-text password
     * @return a validated, ready-to-persist {@link User}
     * @throws IllegalArgumentException if any field fails validation
     */
    public final User getUser(String name, String email, String password) {
        validate(name, email, password);          // shared pre-step
        User user = createUser(name, email, password); // ← factory method call
        user.setActive(true);                     // shared post-step
        return user;
    }

    // ── Shared validation (used by all concrete creators) ────────────────────

    /**
     * Validate mandatory fields.
     * Centralised here so every concrete factory benefits automatically.
     */
    private void validate(String name, String email, String password) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("User name cannot be blank.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("User email is invalid.");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters.");
        }
    }
}
