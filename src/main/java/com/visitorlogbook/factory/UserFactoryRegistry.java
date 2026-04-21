package com.visitorlogbook.factory;

import com.visitorlogbook.model.User;
import com.visitorlogbook.model.UserRole;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/**
 * UserFactoryRegistry – Factory Method Pattern: Registry/Client.
 *
 * Maps each {@link UserRole} to its corresponding {@link UserFactory}
 * (concrete creator) and delegates user-creation requests to it.
 *
 * This is the single entry point callers (e.g. {@code AuthController},
 * {@code AdminController}) use.  They depend only on this registry and
 * the abstract {@link UserFactory}; they never reference any concrete
 * creator subclass directly.
 *
 * ┌─────────────────────────────────────────────────────────────────────┐
 * │              Complete GoF Factory Method Participants               │
 * │                                                                     │
 * │  Product          → User                                            │
 * │  Creator          → UserFactory            (abstract)               │
 * │  ConcreteCreator  → VisitorUserFactory                              │
 * │                     HostUserFactory                                 │
 * │                     SecurityUserFactory                             │
 * │                     AdminUserFactory                                │
 * │  Client           → UserFactoryRegistry  ←── you are here          │
 * └─────────────────────────────────────────────────────────────────────┘
 *
 * Open/Closed in action: to support a new role (e.g. RECEPTIONIST),
 *  1. Add {@code RECEPTIONIST} to {@link UserRole}.
 *  2. Create {@code ReceptionistUserFactory extends UserFactory}.
 *  3. Register it in the constructor below.
 *  — Zero changes to any controller or existing factory.
 */
@Component
public class UserFactoryRegistry {

    /** Internal map: role → its concrete factory. */
    private final Map<UserRole, UserFactory> registry = new EnumMap<>(UserRole.class);

    /**
     * Spring injects all four concrete factories via constructor injection.
     * The qualifier names match the {@code @Component("...")} values on each subclass.
     */
    public UserFactoryRegistry(VisitorUserFactory  visitorFactory,
                               HostUserFactory     hostFactory,
                               SecurityUserFactory securityFactory,
                               AdminUserFactory    adminFactory) {
        registry.put(UserRole.VISITOR,  visitorFactory);
        registry.put(UserRole.HOST,     hostFactory);
        registry.put(UserRole.SECURITY, securityFactory);
        registry.put(UserRole.ADMIN,    adminFactory);
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Create a {@link User} for the given role using the appropriate
     * concrete factory (Factory Method Pattern).
     *
     * @param name     full name
     * @param email    unique email
     * @param password plain-text password (min 4 chars)
     * @param role     the desired {@link UserRole}
     * @return a validated, fully initialised {@link User}
     * @throws IllegalArgumentException if validation fails
     */
    public User create(String name, String email, String password, UserRole role) {
        UserFactory factory = registry.get(role);
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for role: " + role);
        }
        return factory.getUser(name, email, password);   // calls template + factory method
    }

    /**
     * Convenience overload that accepts a raw role string.
     * Delegates to {@link UserRole#fromString(String)} for safe parsing.
     */
    public User create(String name, String email, String password, String roleRaw) {
        return create(name, email, password, UserRole.fromString(roleRaw));
    }

    /**
     * Return the concrete {@link UserFactory} for a given role
     * (useful for direct factory access in tests or special cases).
     */
    public UserFactory getFactory(UserRole role) {
        return registry.get(role);
    }
}
