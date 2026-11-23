package com.mockforge.runtime.storage;

import com.mockforge.project.domain.StorageMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory for creating appropriate DataStore based on storage mode.
 */
@Component
@RequiredArgsConstructor
public class DataStoreFactory {

    private final MemoryDataStore memoryDataStore;
    private final SQLiteDataStore sqliteDataStore;

    /**
     * Get the appropriate data store for the given storage mode.
     */
    public DataStore getDataStore(StorageMode storageMode) {
        return switch (storageMode) {
            case EPHEMERAL -> memoryDataStore;
            case PERSISTENT -> sqliteDataStore;
            case HYBRID -> sqliteDataStore; // For now, HYBRID uses SQLite (can enhance later)
        };
    }
}
