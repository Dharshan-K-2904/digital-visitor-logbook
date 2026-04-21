package com.visitorlogbook.config;

import org.springframework.context.annotation.Configuration;

/**
 * AppConfig – Spring Configuration.
 *
 * Design pattern implementations are auto-discovered by Spring via
 * {@code @Component} / {@code @Service} / {@code @Repository} annotations,
 * so explicit {@code @Bean} factory methods are no longer needed here.
 *
 * Kept as a placeholder for any future application-wide infrastructure beans
 * (e.g. custom Jackson ObjectMapper, MessageSource, TaskExecutor).
 *
 * Design Principles:
 *  - Single Responsibility : purely infrastructure configuration.
 *  - Separation of Concerns: business logic lives in Services; wiring lives here.
 */
@Configuration
public class AppConfig {
    // All pattern beans are registered automatically via class-level annotations:
    //
    //  Singleton : DatabaseConnectionManager  (@Component, Spring singleton scope)
    //  Factory   : UserFactory                (@Component)
    //  Observer  : VisitRequestSubject        (@Component)
    //              NotificationObserver       (@Component)
    //  Strategy  : CsvReportStrategy          (@Component)
    //              PdfReportStrategy          (@Component)
    //              ExcelReportStrategy        (@Component)
    //              ReportContext              (@Component)  ← selects strategy at runtime
    //  DAO       : UserDAOImpl                (@Repository)
    //              VisitRequestDAOImpl        (@Repository)
    //  Service   : UserManagementServiceImpl  (@Service)
    //              VisitRequestServiceImpl    (@Service)
}
