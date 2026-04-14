package com.visitorlogbook.dao;

import com.visitorlogbook.model.User;
import java.util.List;

public interface UserDAO {
    List<User> findAll();
    User findById(Long id);
    User findByEmail(String email);
    List<User> findByRole(String role);
    void save(User user);
    void update(User user);
    void delete(Long id);
    void toggleStatus(Long id);
    long countByRole(String role);
}
