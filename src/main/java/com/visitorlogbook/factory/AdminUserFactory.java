package com.visitorlogbook.factory;

import com.visitorlogbook.model.User;
import org.springframework.stereotype.Component;

/**
 * AdminUserFactory – Concrete Creator (GoF Factory Method Pattern).
 *
 * Produces {@link User} objects with the {@code ADMIN} role.
 * Used internally when seeding the first administrator account.
 *
 * Design Pattern:
 *  - Factory Method : implements the abstract {@link UserFactory#createUser} method.
 */
@Component("adminUserFactory")
public class AdminUserFactory extends UserFactory {

    /**
     * Factory Method implementation – creates an ADMIN user.
     *
     * @param name     admin's full name
     * @param email    admin's unique email address
     * @param password admin's password (min 4 chars, enforced by parent)
     * @return a new {@link User} configured with the ADMIN role
     */
    @Override
    protected User createUser(String name, String email, String password) {
        return new User(name, email, password, "ADMIN");
    }
}
