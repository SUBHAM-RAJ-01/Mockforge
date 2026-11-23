package com.mockforge.project.dto;

import com.mockforge.project.domain.StorageMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO for creating a new project.
 */
@Data
public class CreateProjectRequest {

    @NotBlank(message = "Project name is required")
    @Size(min = 1, max = 100, message = "Project name must be between 1 and 100 characters")
    private String name;

    @Pattern(regexp = "^/[a-zA-Z0-9/_-]*$", message = "Base path must start with / and contain only alphanumeric, -, _, /")
    private String basePath = "/api";

    private StorageMode storageMode = StorageMode.HYBRID;

    private String description;
}
