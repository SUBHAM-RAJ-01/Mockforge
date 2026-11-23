package com.mockforge.runtime.storage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface for storing and retrieving mock data records.
 * Implementations support different storage modes (memory, SQLite, hybrid).
 */
public interface DataStore {

    /**
     * Create a new record.
     * @return The created record with generated ID
     */
    Map<String, Object> create(String resourceId, Map<String, Object> data);

    /**
     * Get a record by ID.
     */
    Optional<Map<String, Object>> findById(String resourceId, String recordId);

    /**
     * Get all records for a resource with optional filtering and pagination.
     */
    List<Map<String, Object>> findAll(String resourceId, QueryOptions options);

    /**
     * Update a record by ID.
     */
    Optional<Map<String, Object>> update(String resourceId, String recordId, Map<String, Object> data);

    /**
     * Delete a record by ID.
     * @return true if deleted, false if not found
     */
    boolean delete(String resourceId, String recordId);

    /**
     * Delete all records for a resource.
     * @return Count of deleted records
     */
    int deleteAll(String resourceId);

    /**
     * Count records for a resource.
     */
    long count(String resourceId);

    /**
     * Check if a record exists.
     */
    boolean exists(String resourceId, String recordId);
}
