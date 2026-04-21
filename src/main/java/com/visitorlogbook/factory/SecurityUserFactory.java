package com.visitorlogbook.factory;

import com.visitorlogbook.model.User;
import org.springframework.stereotype.Component;

/**
 * SecurityUserFactory – Concrete Creator (GoF Factory Method Pattern).
 *
 * Produces {@link User} objects with the {@code SECURITY} role.
 * Used by the admin when creating a security-personnel account.
 *
 * Design Pattern:
 *  - Factory Method : implements the abstract {@link UserFactory#createUser} method.
 */
@Component("securityUserFactory")
public class SecurityUserFactory extends UserFactory {

    /**
     * Factory Method implementation – creates a SECURITY user.
     *
     * @param name     security staff's full name
     * @param email    security staff's unique email address
     * @param password security staff's password (min 4 chars, enforced by parent)
     * @return a new {@link User} configured with the SECURITY role
     */
    @Override
    protected User createUser(String name, String email, String password) {
        return new User(name, email, password, "SECURITY");
    }
}
