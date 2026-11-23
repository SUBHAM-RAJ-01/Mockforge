package com.mockforge.runtime.model;

import com.mockforge.project.domain.StorageMode;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Immutable snapshot of a project's runtime configuration.
 * This is the compiled form used by the dynamic routing engine.
 */
@Data
public class RuntimeSnapshot {

    private final String projectId;
    private final String projectName;
    private final String basePath;
    private final StorageMode storageMode;
    private final Long configVersion;
    private final List<ResourceRuntime> resources;
    private final Map<String, ResourceRuntime> resourcesBySlug;

    /**
     * Find a resource by slug.
     */
    public ResourceRuntime getResource(String slug) {
        return resourcesBySlug.get(slug);
    }

    /**
     * Check if a resource exists.
     */
    public boolean hasResource(String slug) {
        return resourcesBySlug.containsKey(slug);
    }

    /**
     * Get the full path pattern for a resource collection endpoint.
     * Example: /api/users
     */
    public String getCollectionPath(String resourceSlug) {
        return basePath + "/" + resourceSlug;
    }

    /**
     * Get the full path pattern for a resource single-item endpoint.
     * Example: /api/users/{id}
     */
    public String getSingleItemPath(String resourceSlug) {
        return basePath + "/" + resourceSlug + "/{id}";
    }

    @Data
    public static class ResourceRuntime {
        private final String id;
        private final String name;
        private final String slug;
        private final Long schemaVersion;
        private final Boolean timestamps;
        private final List<FieldRuntime> fields;
        private final Map<String, FieldRuntime> fieldsByName;

        public FieldRuntime getField(String name) {
            return fieldsByName.get(name);
        }

        public boolean hasField(String name) {
            return fieldsByName.containsKey(name);
        }
    }

    @Data
    public static class FieldRuntime {
        private final String name;
        private final String type;
        private final Boolean required;
        private final Boolean unique;
        private final Object defaultValue;
        private final Integer position;
    }
}
