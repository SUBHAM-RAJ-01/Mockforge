package com.mockforge.resource.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * Request DTO for updating an existing resource.
 */
@Data
public class UpdateResourceRequest {

    @Size(min = 1, max = 100, message = "Resource name must be between 1 and 100 characters")
    private String name;

    @Valid
    private List<FieldDefinition> fields;

    private Boolean timestamps;
}
