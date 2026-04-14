package com.visitorlogbook.dao.impl;

import com.visitorlogbook.dao.VisitRequestDAO;
import com.visitorlogbook.model.VisitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitRequestDAOImpl implements VisitRequestDAO {

    @Autowired
    private JdbcTemplate jdbc;

    private final RowMapper<VisitRequest> mapper = (rs, i) -> {
        VisitRequest r = new VisitRequest();
        r.setId(rs.getLong("id"));
        r.setVisitorId(rs.getLong("visitor_id"));
        r.setVisitorName(rs.getString("visitor_name"));
        Object hid = rs.getObject("host_id");
        if (hid != null) r.setHostId(((Number) hid).longValue());
        r.setHostName(rs.getString("host_name"));
        r.setPurpose(rs.getString("purpose"));
        if (rs.getDate("visit_date") != null)
            r.setVisitDate(rs.getDate("visit_date").toLocalDate());
        r.setStatus(rs.getString("status"));
        r.setRejectionReason(rs.getString("rejection_reason"));
        if (rs.getTimestamp("check_in")  != null) r.setCheckIn(rs.getTimestamp("check_in").toLocalDateTime());
        if (rs.getTimestamp("check_out") != null) r.setCheckOut(rs.getTimestamp("check_out").toLocalDateTime());
        Object dur = rs.getObject("duration_minutes");
        if (dur != null) r.setDurationMinutes(((Number) dur).longValue());
        if (rs.getTimestamp("created_at") != null) r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return r;
    };

    @Override public List<VisitRequest> findAll() {
        return jdbc.query("SELECT * FROM visit_requests ORDER BY created_at DESC", mapper);
    }
    @Override public VisitRequest findById(Long id) {
        List<VisitRequest> l = jdbc.query("SELECT * FROM visit_requests WHERE id=?", mapper, id);
        return l.isEmpty() ? null : l.get(0);
    }
    @Override public List<VisitRequest> findByVisitorId(Long id) {
        return jdbc.query("SELECT * FROM visit_requests WHERE visitor_id=? ORDER BY created_at DESC", mapper, id);
    }
    @Override public List<VisitRequest> findByHostId(Long id) {
        return jdbc.query("SELECT * FROM visit_requests WHERE host_id=? ORDER BY created_at DESC", mapper, id);
    }
    @Override public List<VisitRequest> findByStatus(String status) {
        return jdbc.query("SELECT * FROM visit_requests WHERE status=? ORDER BY created_at DESC", mapper, status);
    }
    @Override public List<VisitRequest> findFiltered(LocalDate from, LocalDate to, String status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM visit_requests WHERE 1=1");
        List<Object> p = new ArrayList<>();
        if (from   != null) { sql.append(" AND DATE(created_at)>=?"); p.add(from.toString()); }
        if (to     != null) { sql.append(" AND DATE(created_at)<=?"); p.add(to.toString()); }
        if (status != null && !status.isEmpty()) { sql.append(" AND status=?"); p.add(status); }
        sql.append(" ORDER BY created_at DESC");
        return jdbc.query(sql.toString(), mapper, p.toArray());
    }
    @Override public List<VisitRequest> findRecent(int limit) {
        return jdbc.query("SELECT * FROM visit_requests ORDER BY created_at DESC LIMIT ?", mapper, limit);
    }
    @Override public void save(VisitRequest r) {
        jdbc.update(
            "INSERT INTO visit_requests (visitor_id,visitor_name,host_id,host_name,purpose,visit_date,status,created_at) VALUES (?,?,?,?,?,?,?,?)",
            r.getVisitorId(), r.getVisitorName(), r.getHostId(), r.getHostName(),
            r.getPurpose(), r.getVisitDate(), "PENDING", LocalDateTime.now());
    }
    @Override public void updateStatus(Long id, String status, String reason) {
        jdbc.update("UPDATE visit_requests SET status=?,rejection_reason=? WHERE id=?", status, reason, id);
    }
    @Override public void updateCheckIn(Long id) {
        jdbc.update("UPDATE visit_requests SET status='CHECKED_IN',check_in=? WHERE id=?", LocalDateTime.now(), id);
    }
    @Override public void updateCheckOut(Long id) {
        jdbc.update(
            "UPDATE visit_requests SET status='CHECKED_OUT',check_out=?," +
            "duration_minutes=TIMESTAMPDIFF(MINUTE,check_in,NOW()) WHERE id=?",
            LocalDateTime.now(), id);
    }
    @Override public long countByStatus(String status) {
        Long c = jdbc.queryForObject("SELECT COUNT(*) FROM visit_requests WHERE status=?", Long.class, status);
        return c != null ? c : 0L;
    }
    @Override public long countByDate(LocalDate date) {
        Long c = jdbc.queryForObject("SELECT COUNT(*) FROM visit_requests WHERE DATE(created_at)=?", Long.class, date.toString());
        return c != null ? c : 0L;
    }
}
