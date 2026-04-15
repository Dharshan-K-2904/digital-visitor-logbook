package com.visitorlogbook.strategy;

import com.visitorlogbook.model.VisitRequest;
import java.util.List;

/**
 * PdfReportStrategy - Strategy Pattern Implementation
 * Generates visit reports in PDF format.
 */
public class PdfReportStrategy implements ReportStrategy {

    /**
     * Generate a PDF report from visit requests.
     */
    @Override
    public byte[] generateReport(List<VisitRequest> requests) {
        // Placeholder implementation for future Apache iText or PDFBox integration
        StringBuilder content = new StringBuilder();
        content.append("VISIT REQUEST REPORT - PDF\n");
        content.append("============================\n\n");

        for (VisitRequest req : requests) {
            content.append(formatRequestLine(req)).append("\n");
        }

        // In production, convert this to actual PDF bytes using a PDF library
        return content.toString().getBytes();
    }

    @Override
    public String getFileExtension() {
        return "pdf";
    }

    @Override
    public String getMimeType() {
        return "application/pdf";
    }

    /**
     * Format a single visit request line for the report.
     */
    private String formatRequestLine(VisitRequest request) {
        return String.format(
            "ID: %d | Visitor: %s | Host: %s | Status: %s | Purpose: %s",
            request.getId(),
            request.getVisitorName(),
            request.getHostName(),
            request.getStatus(),
            request.getPurpose()
        );
    }
}
