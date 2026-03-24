package com.visitorlogbook.dao;

import com.visitorlogbook.model.Visitor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VisitorDAO {

    private final JdbcTemplate jdbcTemplate;

    public VisitorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Visitor visitor) {
        String sql = "INSERT INTO visitors(user_id,phone,organization) VALUES(?,?,?)";
        jdbcTemplate.update(sql, visitor.getUserId(), visitor.getPhone(), visitor.getOrganization());
    }
}