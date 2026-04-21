package com.visitorlogbook.singleton;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnectionManager – Singleton Pattern (thread-safe, lazy init via Spring).
 *
 * Guarantees a single shared database-configuration object for the entire
 * application lifetime.  Spring's @Component scope is singleton by default,
 * so only one instance is created; this class additionally exposes a
 * classic {@code getInstance()} accessor for code that does not use DI.
 *
 * Design Patterns:
 *  - Singleton : one instance, controlled creation.
 *
 * Design Principles:
 *  - Single Responsibility : owns only connection-config concerns.
 *  - Dependency Inversion  : callers depend on this abstraction, not raw JDBC.
 */
@Component
public class DatabaseConnectionManager {

    // ── Spring-injected values ────────────────────────────────────────────────
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    // ── Classic Singleton holder ──────────────────────────────────────────────
    private static DatabaseConnectionManager INSTANCE;

    /**
     * Called once by Spring after all @Value fields are injected.
     * Registers this Spring-managed bean as the canonical singleton.
     */
    @PostConstruct
    private void init() {
        INSTANCE = this;
    }

    /**
     * Return the single instance.  Usable from static/non-Spring contexts.
     * Returns {@code null} if the Spring context has not yet started.
     */
    public static DatabaseConnectionManager getInstance() {
        return INSTANCE;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Open and return a raw JDBC connection (caller must close it).
     * In production use HikariCP/Spring's DataSource; this is provided
     * purely to demonstrate the Singleton pattern for OOAD study purposes.
     *
     * @return a new {@link Connection} for ad-hoc JDBC usage
     * @throws SQLException on connection failure
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /** Expose the JDBC URL (read-only, for logging/diagnostics). */
    public String getUrl()      { return url; }

    /** Expose the DB username (read-only, for logging/diagnostics). */
    public String getUsername() { return username; }

    @Override
    public String toString() {
        return "DatabaseConnectionManager{url='" + url + "', user='" + username + "'}";
    }
}
