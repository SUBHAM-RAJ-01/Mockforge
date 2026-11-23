package com.mockforge.resource.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Request DTO for creating a new resource.
 */
@Data
public class CreateResourceRequest {

    @NotBlank(message = "Resource name is required")
    @Size(min = 1, max = 100, message = "Resource name must be between 1 and 100 characters")
    private String name;

    @Valid
    @NotNull(message = "Fields are required")
    @Size(min = 1, message = "At least one field is required")
    private List<FieldDefinition> fields = new ArrayList<>();

    private Boolean timestamps = true;
}
