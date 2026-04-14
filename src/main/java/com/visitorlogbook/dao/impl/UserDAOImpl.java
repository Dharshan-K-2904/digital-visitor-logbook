package com.visitorlogbook.dao.impl;

import com.visitorlogbook.dao.UserDAO;
import com.visitorlogbook.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbc;

    private final RowMapper<User> mapper = (rs, i) -> {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setActive(rs.getBoolean("active"));
        if (rs.getTimestamp("created_at") != null)
            u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return u;
    };

    @Override public List<User> findAll() {
        return jdbc.query("SELECT * FROM users ORDER BY created_at DESC", mapper);
    }
    @Override public User findById(Long id) {
        List<User> l = jdbc.query("SELECT * FROM users WHERE id=?", mapper, id);
        return l.isEmpty() ? null : l.get(0);
    }
    @Override public User findByEmail(String email) {
        List<User> l = jdbc.query("SELECT * FROM users WHERE email=?", mapper, email);
        return l.isEmpty() ? null : l.get(0);
    }
    @Override public List<User> findByRole(String role) {
        return jdbc.query("SELECT * FROM users WHERE role=? ORDER BY name", mapper, role);
    }
    @Override public void save(User u) {
        jdbc.update("INSERT INTO users (name,email,password,role,active) VALUES (?,?,?,?,1)",
                u.getName(), u.getEmail(), u.getPassword(), u.getRole());
    }
    @Override public void update(User u) {
        jdbc.update("UPDATE users SET name=?,email=?,role=?,active=? WHERE id=?",
                u.getName(), u.getEmail(), u.getRole(), u.isActive(), u.getId());
    }
    @Override public void delete(Long id) {
        jdbc.update("DELETE FROM users WHERE id=?", id);
    }
    @Override public void toggleStatus(Long id) {
        jdbc.update("UPDATE users SET active = NOT active WHERE id=?", id);
    }
    @Override public long countByRole(String role) {
        Long c = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE role=?", Long.class, role);
        return c != null ? c : 0L;
    }
}
