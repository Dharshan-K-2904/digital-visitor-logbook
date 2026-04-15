package com.visitorlogbook.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * VisitRequest DTO - Data Transfer Object for visit request information.
 * 
 * Used by DAO layer for JDBC-based database access.
 * Statuses: PENDING, APPROVED, REJECTED, CHECKED_IN, CHECKED_OUT
 */
public class VisitRequest {
    private Long id;
    private Long visitorId;
    private String visitorName;
    private Long hostId;
    private String hostName;
    private String purpose;
    private LocalDate visitDate;
    private String status;              // PENDING | APPROVED | REJECTED | CHECKED_IN | CHECKED_OUT
    private String rejectionReason;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Long durationMinutes;
    private LocalDateTime createdAt;

    // ===== CONSTRUCTORS & ACCESSORS =====
    
    public VisitRequest() {}
    public Long getId()                                   { return id; }
    public void setId(Long id)                            { this.id = id; }
    public Long getVisitorId()                            { return visitorId; }
    public void setVisitorId(Long v)                      { this.visitorId = v; }
    public String getVisitorName()                        { return visitorName; }
    public void setVisitorName(String v)                  { this.visitorName = v; }
    public Long getHostId()                               { return hostId; }
    public void setHostId(Long h)                         { this.hostId = h; }
    public String getHostName()                           { return hostName; }
    public void setHostName(String h)                     { this.hostName = h; }
    public String getPurpose()                            { return purpose; }
    public void setPurpose(String p)                      { this.purpose = p; }
    public LocalDate getVisitDate()                       { return visitDate; }
    public void setVisitDate(LocalDate d)                 { this.visitDate = d; }
    public String getStatus()                             { return status; }
    public void setStatus(String s)                       { this.status = s; }
    public String getRejectionReason()                    { return rejectionReason; }
    public void setRejectionReason(String r)              { this.rejectionReason = r; }
    public LocalDateTime getCheckIn()                     { return checkIn; }
    public void setCheckIn(LocalDateTime t)               { this.checkIn = t; }
    public LocalDateTime getCheckOut()                    { return checkOut; }
    public void setCheckOut(LocalDateTime t)              { this.checkOut = t; }
    public Long getDurationMinutes()                      { return durationMinutes; }
    public void setDurationMinutes(Long d)                { this.durationMinutes = d; }
    public LocalDateTime getCreatedAt()                   { return createdAt; }
    public void setCreatedAt(LocalDateTime t)             { this.createdAt = t; }
}
