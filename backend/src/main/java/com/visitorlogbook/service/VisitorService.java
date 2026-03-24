package com.visitorlogbook.service;

import com.visitorlogbook.dao.UserDAO;
import com.visitorlogbook.dao.VisitorDAO;
import com.visitorlogbook.model.User;
import com.visitorlogbook.model.Visitor;
import org.springframework.stereotype.Service;

@Service
public class VisitorService {

    private final AuthService authService;
    private final UserDAO userDAO;
    private final VisitorDAO visitorDAO;

    public VisitorService(AuthService authService, UserDAO userDAO, VisitorDAO visitorDAO) {
        this.authService = authService;
        this.userDAO = userDAO;
        this.visitorDAO = visitorDAO;
    }

    public boolean registerVisitor(User user, Visitor visitor) {

        if (userDAO.findByEmail(user.getEmail()) != null) {
            return false;
        }

        authService.register(user);

        User savedUser = userDAO.findByEmail(user.getEmail());
        visitor.setUserId(savedUser.getId());

        visitorDAO.save(visitor);

        return true;
    }
}