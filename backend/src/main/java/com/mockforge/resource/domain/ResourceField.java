package com.mockforge.resource.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ResourceField entity representing a field within a resource schema.
 */
@Entity
@Table(name = "resource_fields")
@Data
@NoArgsConstructor
public class ResourceField {

    @Id
    @Column(nullable = false, length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type", nullable = false, length = 20)
    private FieldType fieldType;

    @Column(nullable = false)
    private Boolean required = false;

    @Column(name = "unique_flag", nullable = false)
    private Boolean uniqueFlag = false;

    @Column(name = "default_json", length = 1000)
    private String defaultJson;

    @Column(name = "config_json", length = 2000)
    private String configJson;

    @Column(nullable = false)
    private Integer position;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
