package com.visitorlogbook.dao;

import com.visitorlogbook.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {

    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        String sql = "INSERT INTO users(name,email,password,role) VALUES(?,?,?,?)";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }

    public User findByEmail(String email) {
        try {
            String sql = "SELECT * FROM users WHERE email=?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                return user;
            }, email);
        } catch (Exception e) {
            return null;
        }
    }
}