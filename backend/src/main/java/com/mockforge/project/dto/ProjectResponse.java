package com.mockforge.project.dto;

import com.mockforge.project.domain.Project;
import com.mockforge.project.domain.ProjectStatus;
import com.mockforge.project.domain.StorageMode;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response DTO for project data.
 */
@Data
public class ProjectResponse {

    private String id;
    private String name;
    private String slug;
    private String basePath;
    private StorageMode storageMode;
    private ProjectStatus status;
    private String description;
    private Long configVersion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProjectResponse from(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setSlug(project.getSlug());
        response.setBasePath(project.getBasePath());
        response.setStorageMode(project.getStorageMode());
        response.setStatus(project.getStatus());
        response.setConfigVersion(project.getConfigVersion());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        return response;
    }
}
