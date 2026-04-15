package com.visitorlogbook.config;

import com.visitorlogbook.factory.UserFactory;
import com.visitorlogbook.observer.NotificationObserver;
import com.visitorlogbook.strategy.ExcelReportStrategy;
import com.visitorlogbook.strategy.PdfReportStrategy;
import com.visitorlogbook.strategy.ReportStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AppConfig - Spring Configuration
 * Centralizes configuration for beans, design pattern implementations,
 * and application-wide settings.
 */
@Configuration
public class AppConfig {

    /**
     * Configure UserFactory bean.
     */
    @Bean
    public UserFactory userFactory() {
        return new UserFactory();
    }

    /**
     * Configure NotificationObserver bean.
     */
    @Bean
    public NotificationObserver notificationObserver() {
        return new NotificationObserver();
    }

    /**
     * Configure PDF Report Strategy bean.
     */
    @Bean
    public ReportStrategy pdfReportStrategy() {
        return new PdfReportStrategy();
    }

    /**
     * Configure Excel Report Strategy bean.
     */
    @Bean
    public ReportStrategy excelReportStrategy() {
        return new ExcelReportStrategy();
    }

    /**
     * Configure JdbcTemplate for database operations.
     * Note: JdbcTemplate is auto-configured by Spring, but can be customized here if needed.
     */
    // Additional configuration can be added here as needed
}
