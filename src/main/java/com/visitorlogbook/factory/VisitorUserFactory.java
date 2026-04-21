package com.visitorlogbook.factory;

import com.visitorlogbook.model.User;
import org.springframework.stereotype.Component;

/**
 * VisitorUserFactory – Concrete Creator (GoF Factory Method Pattern).
 *
 * Produces {@link User} objects with the {@code VISITOR} role.
 * Used when a new visitor self-registers via the public registration form.
 *
 * Design Pattern:
 *  - Factory Method : implements the abstract {@link UserFactory#createUser} method.
 */
@Component("visitorUserFactory")
public class VisitorUserFactory extends UserFactory {

    /**
     * Factory Method implementation – creates a VISITOR user.
     *
     * @param name     visitor's full name
     * @param email    visitor's unique email address
     * @param password visitor's password (min 4 chars, enforced by parent)
     * @return a new {@link User} configured with the VISITOR role
     */
    @Override
    protected User createUser(String name, String email, String password) {
        return new User(name, email, password, "VISITOR");
    }
}
