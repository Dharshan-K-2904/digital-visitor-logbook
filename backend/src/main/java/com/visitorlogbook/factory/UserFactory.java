package com.visitorlogbook.factory;

import com.visitorlogbook.model.User;

public class UserFactory {
    public static User createUser(String role) {
        User user = new User();
        user.setRole(role);
        return user;
    }
}