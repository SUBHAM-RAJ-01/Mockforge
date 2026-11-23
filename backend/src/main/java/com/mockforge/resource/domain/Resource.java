package com.mockforge.resource.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Resource entity representing a data model within a project.
 * Each resource defines a schema with fields and can have CRUD endpoints generated.
 */
@Entity
@Table(name = "resources")
@Data
@NoArgsConstructor
public class Resource {

    @Id
    @Column(nullable = false, length = 50)
    private String id;

    @Column(name = "project_id", nullable = false, length = 50)
    private String projectId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String slug;

    @Column(name = "schema_version", nullable = false)
    private Long schemaVersion = 1L;

    @Column(nullable = false)
    private Boolean timestamps = true;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("position ASC")
    private List<ResourceField> fields = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Add a field to this resource.
     */
    public void addField(ResourceField field) {
        fields.add(field);
        field.setResource(this);
    }

    /**
     * Remove a field from this resource.
     */
    public void removeField(ResourceField field) {
        fields.remove(field);
        field.setResource(null);
    }

    /**
     * Increment the schema version (called when fields change).
     */
    public void incrementSchemaVersion() {
        this.schemaVersion++;
    }
}
