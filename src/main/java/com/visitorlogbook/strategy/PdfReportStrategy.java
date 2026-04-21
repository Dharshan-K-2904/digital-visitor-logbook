package com.visitorlogbook.strategy;

import com.visitorlogbook.model.VisitRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PdfReportStrategy – Strategy Pattern: generates text-based PDF reports.
 *
 * This implementation produces a human-readable text representation.
 * In a production system replace {@code generateReport} body with
 * Apache PDFBox or iText integration — the interface contract stays identical
 * so the controller and context require zero changes (Open/Closed Principle).
 *
 * Design Patterns:
 *  - Strategy : concrete PDF report generation algorithm.
 *
 * Design Principles:
 *  - SRP : only responsible for PDF formatting logic.
 *  - OCP : production upgrade = swap this body; interface is stable.
 */
@Component
public class PdfReportStrategy implements ReportStrategy {

    private static final String SEPARATOR = "─".repeat(70);

    @Override
    public byte[] generateReport(List<VisitRequest> requests) {
        StringBuilder sb = new StringBuilder();
        sb.append("DIGITAL VISITOR LOGBOOK – VISIT REPORT\n");
        sb.append(SEPARATOR).append("\n");
        sb.append(String.format("Total Records: %d%n%n", requests.size()));

        for (VisitRequest r : requests) {
            sb.append(String.format(
                    "ID     : %d%nVisitor: %s%nHost   : %s%nPurpose: %s%n" +
                    "Date   : %s%nStatus : %s%nCheck-In : %s%nCheck-Out: %s%n" +
                    "Duration : %s min%n",
                    r.getId(),
                    safe(r.getVisitorName()),
                    safe(r.getHostName()),
                    safe(r.getPurpose()),
                    safe(r.getVisitDate()),
                    safe(r.getStatus()),
                    safe(r.getCheckIn()),
                    safe(r.getCheckOut()),
                    safe(r.getDurationMinutes())
            ));
            sb.append(SEPARATOR).append("\n");
        }

        // TODO: Integrate Apache PDFBox to produce actual PDF bytes.
        return sb.toString().getBytes();
    }

    @Override public String getFileExtension() { return "pdf"; }
    @Override public String getMimeType()       { return "application/pdf"; }

    private String safe(Object v) { return v == null ? "—" : v.toString(); }
}
