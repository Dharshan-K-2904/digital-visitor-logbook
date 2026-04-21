package com.visitorlogbook.factory;

import com.visitorlogbook.model.User;
import org.springframework.stereotype.Component;

/**
 * HostUserFactory – Concrete Creator (GoF Factory Method Pattern).
 *
 * Produces {@link User} objects with the {@code HOST} role.
 * Used by the admin when creating a host (employee) account.
 *
 * Design Pattern:
 *  - Factory Method : implements the abstract {@link UserFactory#createUser} method.
 */
@Component("hostUserFactory")
public class HostUserFactory extends UserFactory {

    /**
     * Factory Method implementation – creates a HOST user.
     *
     * @param name     host's full name
     * @param email    host's unique email address
     * @param password host's password (min 4 chars, enforced by parent)
     * @return a new {@link User} configured with the HOST role
     */
    @Override
    protected User createUser(String name, String email, String password) {
        return new User(name, email, password, "HOST");
    }
}
