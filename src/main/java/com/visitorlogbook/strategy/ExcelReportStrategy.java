package com.visitorlogbook.strategy;

import com.visitorlogbook.model.VisitRequest;
import java.util.List;

/**
 * ExcelReportStrategy - Strategy Pattern Implementation
 * Generates visit reports in Excel format.
 */
public class ExcelReportStrategy implements ReportStrategy {

    /**
     * Generate an Excel report from visit requests.
     */
    @Override
    public byte[] generateReport(List<VisitRequest> requests) {
        // Placeholder implementation for future Apache POI integration
        StringBuilder content = new StringBuilder();
        content.append("ID,Visitor Name,Host Name,Status,Purpose,Visit Date,Check In,Check Out\n");

        for (VisitRequest req : requests) {
            content.append(formatRequestAsCSV(req)).append("\n");
        }

        // In production, convert this to actual Excel (.xlsx) bytes using Apache POI
        return content.toString().getBytes();
    }

    @Override
    public String getFileExtension() {
        return "xlsx";
    }

    @Override
    public String getMimeType() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    /**
     * Format a single visit request as CSV row for Excel export.
     */
    private String formatRequestAsCSV(VisitRequest request) {
        return String.format(
            "%d,%s,%s,%s,%s,%s,%s,%s",
            request.getId(),
            escapeCSV(request.getVisitorName()),
            escapeCSV(request.getHostName()),
            request.getStatus(),
            escapeCSV(request.getPurpose()),
            request.getVisitDate(),
            request.getCheckIn(),
            request.getCheckOut()
        );
    }

    /**
     * Escape CSV special characters.
     */
    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
