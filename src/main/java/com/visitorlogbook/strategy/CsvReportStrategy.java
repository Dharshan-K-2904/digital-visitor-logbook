package com.visitorlogbook.strategy;

import com.visitorlogbook.model.VisitRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CsvReportStrategy – Strategy Pattern: generates comma-separated value reports.
 *
 * This is a concrete strategy; {@link ReportContext} selects it when the
 * requested format is {@code "csv"}.  The controller never references this
 * class directly (Dependency Inversion Principle).
 *
 * Design Patterns:
 *  - Strategy : concrete algorithm for CSV report generation.
 *
 * Design Principles:
 *  - SRP : only responsible for CSV formatting logic.
 *  - OCP : new column requirements → subclass or new strategy; this is stable.
 */
@Component
public class CsvReportStrategy implements ReportStrategy {

    private static final String HEADER =
            "ID,Visitor Name,Host Name,Status,Purpose,Visit Date,Check In,Check Out,Duration (min)\n";

    @Override
    public byte[] generateReport(List<VisitRequest> requests) {
        StringBuilder sb = new StringBuilder(HEADER);
        for (VisitRequest r : requests) {
            sb.append(row(r)).append("\n");
        }
        return sb.toString().getBytes();
    }

    @Override public String getFileExtension() { return "csv"; }
    @Override public String getMimeType()       { return "text/csv"; }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private String row(VisitRequest r) {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s",
                r.getId(),
                escape(r.getVisitorName()),
                escape(r.getHostName()),
                safe(r.getStatus()),
                escape(r.getPurpose()),
                safe(r.getVisitDate()),
                safe(r.getCheckIn()),
                safe(r.getCheckOut()),
                safe(r.getDurationMinutes())
        );
    }

    private String escape(String v) {
        if (v == null) return "";
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            return "\"" + v.replace("\"", "\"\"") + "\"";
        }
        return v;
    }

    private String safe(Object v) { return v == null ? "" : v.toString(); }
}
