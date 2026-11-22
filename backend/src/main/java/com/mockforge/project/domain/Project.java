package com.mockforge.project.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Project entity representing a mock API project.
 * A project is the top-level container for resources, endpoints, and configuration.
 */
@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
public class Project {

    @Id
    @Column(nullable = false, length = 50)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "base_path", nullable = false)
    private String basePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_mode", nullable = false)
    private StorageMode storageMode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.STOPPED;

    @Column(name = "config_version", nullable = false)
    private Long configVersion = 1L;

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

    public void incrementConfigVersion() {
        this.configVersion++;
    }
}
