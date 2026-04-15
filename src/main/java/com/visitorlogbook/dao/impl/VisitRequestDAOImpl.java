package com.visitorlogbook.dao.impl;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.visitorlogbook.dao.VisitRequestDAO;
import com.visitorlogbook.model.VisitRequest;

@Repository
public class VisitRequestDAOImpl implements VisitRequestDAO {

    private final JdbcTemplate jdbcTemplate;

    public VisitRequestDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Java 21 + Spring Boot 3.5 compatible
     * Fully warning-free RowMapper
     */
    private final @NonNull RowMapper<VisitRequest> mapper =
            (@NonNull ResultSet rs, int rowNum) -> {
                VisitRequest r = new VisitRequest();

                r.setId(rs.getLong("id"));
                r.setVisitorId(rs.getLong("visitor_id"));
                r.setVisitorName(rs.getString("visitor_name"));

                Object hid = rs.getObject("host_id");
                if (hid != null) {
                    r.setHostId(((Number) hid).longValue());
                }

                r.setHostName(rs.getString("host_name"));
                r.setPurpose(rs.getString("purpose"));

                if (rs.getDate("visit_date") != null) {
                    r.setVisitDate(rs.getDate("visit_date").toLocalDate());
                }

                r.setStatus(rs.getString("status"));
                r.setRejectionReason(rs.getString("rejection_reason"));

                if (rs.getTimestamp("check_in") != null) {
                    r.setCheckIn(rs.getTimestamp("check_in").toLocalDateTime());
                }

                if (rs.getTimestamp("check_out") != null) {
                    r.setCheckOut(rs.getTimestamp("check_out").toLocalDateTime());
                }

                Object dur = rs.getObject("duration_minutes");
                if (dur != null) {
                    r.setDurationMinutes(((Number) dur).longValue());
                }

                if (rs.getTimestamp("created_at") != null) {
                    r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }

                return r;
            };

    @Override
    public List<VisitRequest> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM visit_requests ORDER BY created_at DESC",
                mapper
        );
    }

    @Override
    public VisitRequest findById(Long id) {
        List<VisitRequest> list = jdbcTemplate.query(
                "SELECT * FROM visit_requests WHERE id=?",
                mapper,
                id
        );
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<VisitRequest> findByVisitorId(Long id) {
        return jdbcTemplate.query(
                "SELECT * FROM visit_requests WHERE visitor_id=? ORDER BY created_at DESC",
                mapper,
                id
        );
    }

    @Override
    public List<VisitRequest> findByHostId(Long id) {
        return jdbcTemplate.query(
                "SELECT * FROM visit_requests WHERE host_id=? ORDER BY created_at DESC",
                mapper,
                id
        );
    }

    @Override
    public List<VisitRequest> findByStatus(String status) {
        return jdbcTemplate.query(
                "SELECT * FROM visit_requests WHERE status=? ORDER BY created_at DESC",
                mapper,
                status
        );
    }

    @Override
    public List<VisitRequest> findFiltered(LocalDate from, LocalDate to, String status) {

        StringBuilder sqlBuilder =
                new StringBuilder("SELECT * FROM visit_requests WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (from != null) {
            sqlBuilder.append(" AND DATE(created_at) >= ?");
            params.add(from);
        }

        if (to != null) {
            sqlBuilder.append(" AND DATE(created_at) <= ?");
            params.add(to);
        }

        if (status != null && !status.isBlank()) {
            sqlBuilder.append(" AND status = ?");
            params.add(status);
        }

        sqlBuilder.append(" ORDER BY created_at DESC");

        return jdbcTemplate.query(
                Objects.requireNonNull(sqlBuilder.toString()), 
                mapper, 
                params.toArray()
        );
    }

    @Override
    public List<VisitRequest> findRecent(int limit) {
        return jdbcTemplate.query(
                "SELECT * FROM visit_requests ORDER BY created_at DESC LIMIT ?",
                mapper,
                limit
        );
    }

    @Override
    public void save(VisitRequest r) {
        jdbcTemplate.update(
                """
                INSERT INTO visit_requests
                (visitor_id, visitor_name, host_id, host_name, purpose, visit_date, status, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """,
                r.getVisitorId(),
                r.getVisitorName(),
                r.getHostId(),
                r.getHostName(),
                r.getPurpose(),
                r.getVisitDate(),
                "PENDING",
                LocalDateTime.now()
        );
    }

    @Override
    public void updateStatus(Long id, String status, String reason) {
        jdbcTemplate.update(
                "UPDATE visit_requests SET status=?, rejection_reason=? WHERE id=?",
                status,
                reason,
                id
        );
    }

    @Override
    public void updateCheckIn(Long id) {
        jdbcTemplate.update(
                "UPDATE visit_requests SET status='CHECKED_IN', check_in=? WHERE id=?",
                LocalDateTime.now(),
                id
        );
    }

    @Override
    public void updateCheckOut(Long id) {
        jdbcTemplate.update(
                """
                UPDATE visit_requests
                SET status='CHECKED_OUT',
                    check_out=?,
                    duration_minutes=TIMESTAMPDIFF(MINUTE, check_in, NOW())
                WHERE id=?
                """,
                LocalDateTime.now(),
                id
        );
    }

    @Override
    public long countByStatus(String status) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM visit_requests WHERE status=?",
                Long.class,
                status
        );
        return count != null ? count : 0L;
    }

    @Override
    public long countByDate(LocalDate date) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM visit_requests WHERE DATE(created_at)=?",
                Long.class,
                date
        );
        return count != null ? count : 0L;
    }
}