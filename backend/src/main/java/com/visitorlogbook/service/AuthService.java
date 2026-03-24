package com.visitorlogbook.service;

import com.visitorlogbook.dao.UserDAO;
import com.visitorlogbook.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    public boolean login(String email, String password) {
        User user = userDAO.findByEmail(email);
        return user != null && encoder.matches(password, user.getPassword());
    }

    public User getUser(String email) {
        return userDAO.findByEmail(email);
    }
}