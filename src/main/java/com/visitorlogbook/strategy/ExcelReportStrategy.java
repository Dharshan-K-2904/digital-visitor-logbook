package com.visitorlogbook.strategy;

import com.visitorlogbook.model.VisitRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ExcelReportStrategy – Strategy Pattern: generates Excel-compatible CSV reports.
 *
 * The file extension is {@code xlsx} so browsers/OS open it in Excel/Sheets.
 * Actual binary .xlsx generation requires Apache POI; this produces a
 * UTF-8 CSV body accepted by Excel when the MIME type is set correctly.
 *
 * Design Patterns:
 *  - Strategy : concrete Excel/spreadsheet report generation algorithm.
 *
 * Design Principles:
 *  - SRP : only responsible for Excel formatting logic.
 *  - OCP : integrate Apache POI by swapping generateReport body; interface stable.
 */
@Component
public class ExcelReportStrategy implements ReportStrategy {

    /** Excel-friendly UTF-8 BOM so it opens without encoding issues. */
    private static final String BOM = "\uFEFF";

    private static final String HEADER =
            "ID\tVisitor Name\tHost Name\tStatus\tPurpose\tVisit Date\tCheck In\tCheck Out\tDuration (min)\n";

    @Override
    public byte[] generateReport(List<VisitRequest> requests) {
        // Tab-separated (TSV) opens natively in Excel without import wizard.
        StringBuilder sb = new StringBuilder(BOM).append(HEADER);
        for (VisitRequest r : requests) {
            sb.append(String.format("%d\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
                    r.getId(),
                    safe(r.getVisitorName()),
                    safe(r.getHostName()),
                    safe(r.getStatus()),
                    safe(r.getPurpose()),
                    safe(r.getVisitDate()),
                    safe(r.getCheckIn()),
                    safe(r.getCheckOut()),
                    safe(r.getDurationMinutes())
            ));
        }
        return sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    @Override public String getFileExtension() { return "xlsx"; }
    @Override public String getMimeType() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    private String safe(Object v) { return v == null ? "" : v.toString().replace("\t", " "); }
}
