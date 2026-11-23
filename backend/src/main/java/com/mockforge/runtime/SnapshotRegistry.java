package com.mockforge.runtime;

import com.mockforge.runtime.model.RuntimeSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for active runtime snapshots.
 * Provides atomic snapshot replacement for zero-downtime configuration changes.
 */
@Component
@Slf4j
public class SnapshotRegistry {

    private final Map<String, RuntimeSnapshot> snapshotsByProjectId = new ConcurrentHashMap<>();

    /**
     * Register or replace a snapshot for a project.
     * This operation is atomic - requests will see either the old or new snapshot.
     */
    public void registerSnapshot(RuntimeSnapshot snapshot) {
        String projectId = snapshot.getProjectId();
        RuntimeSnapshot old = snapshotsByProjectId.put(projectId, snapshot);
        
        if (old != null) {
            log.info("Replaced snapshot for project {}: v{} -> v{}", 
                    projectId, old.getConfigVersion(), snapshot.getConfigVersion());
        } else {
            log.info("Registered new snapshot for project {}: v{}", 
                    projectId, snapshot.getConfigVersion());
        }
    }

    /**
     * Get the active snapshot for a project.
     */
    public Optional<RuntimeSnapshot> getSnapshot(String projectId) {
        return Optional.ofNullable(snapshotsByProjectId.get(projectId));
    }

    /**
     * Remove a project's snapshot (e.g., when project is deleted).
     */
    public void removeSnapshot(String projectId) {
        RuntimeSnapshot removed = snapshotsByProjectId.remove(projectId);
        if (removed != null) {
            log.info("Removed snapshot for project {}", projectId);
        }
    }

    /**
     * Find a snapshot by matching the base path.
     */
    public Optional<RuntimeSnapshot> findByBasePath(String path) {
        return snapshotsByProjectId.values().stream()
                .filter(snapshot -> path.startsWith(snapshot.getBasePath() + "/"))
                .findFirst();
    }

    /**
     * Check if any snapshot is registered.
     */
    public boolean hasSnapshots() {
        return !snapshotsByProjectId.isEmpty();
    }

    /**
     * Get count of active snapshots.
     */
    public int getSnapshotCount() {
        return snapshotsByProjectId.size();
    }
}
