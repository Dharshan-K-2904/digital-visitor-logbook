package com.visitorlogbook.strategy;

import com.visitorlogbook.model.VisitRequest;
import java.util.List;

/**
 * ReportStrategy Interface - Strategy Pattern
 * Defines the contract for different report generation strategies.
 * Allows interchangeable report generation behaviors.
 */
public interface ReportStrategy {
    /**
     * Generate a report from visit requests.
     *
     * @param requests the list of visit requests to include in the report
     * @return the generated report as a byte array (for PDF/Excel)
     */
    byte[] generateReport(List<VisitRequest> requests);

    /**
     * Get the file extension for this report format.
     *
     * @return the file extension (e.g., "pdf", "xlsx")
     */
    String getFileExtension();

    /**
     * Get the MIME type for this report format.
     *
     * @return the MIME type (e.g., "application/pdf")
     */
    String getMimeType();
}
