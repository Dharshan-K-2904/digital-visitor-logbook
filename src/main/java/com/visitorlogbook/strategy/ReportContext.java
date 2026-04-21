package com.visitorlogbook.strategy;

import com.visitorlogbook.model.VisitRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ReportContext – Strategy Pattern: Context class.
 *
 * Holds all registered {@link ReportStrategy} implementations and selects
 * the correct one at runtime based on a format string.
 * No caller needs to know which concrete strategy is used (OCP, DIP).
 *
 * To add a new format (e.g. XML):
 *  1. Implement {@link ReportStrategy}.
 *  2. Annotate it with {@code @Component}.
 *  Spring auto-registers it here — zero changes to existing code.
 *
 * Design Patterns:
 *  - Strategy : runtime selection of report-generation algorithm.
 *
 * Design Principles:
 *  - Open/Closed          : new formats → new class; this class unchanged.
 *  - Dependency Inversion : depends on the {@link ReportStrategy} abstraction.
 *  - Single Responsibility: only responsible for strategy selection/dispatch.
 */
@Component
public class ReportContext {

    /** Registry: format key → strategy instance. */
    private final Map<String, ReportStrategy> strategies;

    /**
     * Spring injects ALL {@link ReportStrategy} beans via the List constructor.
     * Each strategy self-declares its format key via {@link ReportStrategy#getFileExtension()}.
     */
    public ReportContext(List<ReportStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        s -> s.getFileExtension().toLowerCase(),
                        Function.identity()
                ));
    }

    /**
     * Generate a report in the requested format.
     *
     * @param format  the file extension key (e.g. {@code "csv"}, {@code "pdf"}).
     *                Falls back to CSV if the format is unknown.
     * @param records the visit request records to include.
     * @return report bytes ready to stream to the client.
     */
    public byte[] generate(String format, List<VisitRequest> records) {
        ReportStrategy strategy = strategies.getOrDefault(
                format != null ? format.toLowerCase() : "csv",
                strategies.get("csv")
        );
        return strategy.generateReport(records);
    }

    /**
     * Return a specific strategy for metadata (MIME type, extension).
     * Defaults to CSV if format is unrecognised.
     */
    public ReportStrategy getStrategy(String format) {
        return strategies.getOrDefault(
                format != null ? format.toLowerCase() : "csv",
                strategies.get("csv")
        );
    }

    /** Return all registered format keys (for UI display). */
    public Set<String> availableFormats() {
        return strategies.keySet();
    }
}
