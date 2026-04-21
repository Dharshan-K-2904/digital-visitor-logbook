package com.visitorlogbook.model;

/**
 * UserRole Enum – Open/Closed Principle.
 *
 * Replaces raw String literals for roles throughout the system.
 * To support a new role, ADD a constant here without modifying any
 * existing switch/validation logic that already delegates to this enum.
 *
 * Design Principles:
 *  - Open/Closed  : new roles extend this enum; nothing else changes.
 *  - Single Responsibility : one place owns all valid role definitions.
 */
public enum UserRole {

    VISITOR("Visitor Portal",     "/visitor/dashboard"),
    HOST   ("Host Portal",        "/host/dashboard"),
    SECURITY("Security Portal",   "/security/dashboard"),
    ADMIN  ("Admin Portal",       "/admin/dashboard");

    /** Human-readable portal label shown in the sidebar. */
    private final String label;

    /** Default redirect path after login. */
    private final String defaultPath;

    UserRole(String label, String defaultPath) {
        this.label       = label;
        this.defaultPath = defaultPath;
    }

    public String getLabel()       { return label; }
    public String getDefaultPath() { return defaultPath; }

    /**
     * Safe conversion from a raw String (case-insensitive).
     * Returns {@code VISITOR} if the value is unknown, preventing crashes.
     */
    public static UserRole fromString(String raw) {
        if (raw == null) return VISITOR;
        try {
            return valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return VISITOR;
        }
    }
}
