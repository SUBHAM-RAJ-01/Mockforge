package com.mockforge.project.domain;

/**
 * Storage modes for mock data in a project.
 */
public enum StorageMode {
    /**
     * All data exists only in memory. Lost on restart.
     * Fast, suitable for temporary testing.
     */
    EPHEMERAL,

    /**
     * All data persists in SQLite database.
     * Survives restarts. Safe for important test data.
     */
    PERSISTENT,

    /**
     * Hybrid: SQLite source of truth, with in-memory cache.
     * Best of both worlds for performance + durability.
     */
    HYBRID
}
