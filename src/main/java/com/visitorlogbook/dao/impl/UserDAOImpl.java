package com.visitorlogbook.dao.impl;

import java.sql.ResultSet;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.visitorlogbook.dao.UserDAO;
import com.visitorlogbook.model.User;

@Repository
public class UserDAOImpl implements UserDAO {

    private final JdbcTemplate jdbcTemplate;

    public UserDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final @NonNull RowMapper<User> mapper =
            (@NonNull ResultSet rs, int rowNum) -> {
                User u = new User();

                u.setId(rs.getLong("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                u.setActive(rs.getBoolean("active"));

                if (rs.getTimestamp("created_at") != null) {
                    u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }

                return u;
            };

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users ORDER BY created_at DESC",
                mapper
        );
    }

    @Override
    public User findById(Long id) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users WHERE id=?",
                mapper,
                id
        );
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User findByEmail(String email) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users WHERE email=?",
                mapper,
                email
        );
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findByRole(String role) {
        return jdbcTemplate.query(
                "SELECT * FROM users WHERE role=? ORDER BY name",
                mapper,
                role
        );
    }

    @Override
    public void save(User u) {
        jdbcTemplate.update(
                "INSERT INTO users (name,email,password,role,active) VALUES (?,?,?,?,1)",
                u.getName(),
                u.getEmail(),
                u.getPassword(),
                u.getRole()
        );
    }

    @Override
    public void update(User u) {
        jdbcTemplate.update(
                "UPDATE users SET name=?,email=?,role=?,active=? WHERE id=?",
                u.getName(),
                u.getEmail(),
                u.getRole(),
                u.isActive(),
                u.getId()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
    }

    @Override
    public void toggleStatus(Long id) {
        jdbcTemplate.update("UPDATE users SET active = NOT active WHERE id=?", id);
    }

    @Override
    public long countByRole(String role) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE role=?",
                Long.class,
                role
        );
        return count != null ? count : 0L;
    }
}