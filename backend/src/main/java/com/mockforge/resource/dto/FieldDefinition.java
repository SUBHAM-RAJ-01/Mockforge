package com.mockforge.resource.dto;

import com.mockforge.resource.domain.FieldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO for defining a field in a resource schema.
 */
@Data
public class FieldDefinition {

    @NotBlank(message = "Field name is required")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "Field name must start with a letter and contain only alphanumeric and underscore")
    private String name;

    @NotNull(message = "Field type is required")
    private FieldType type;

    private Boolean required = false;

    private Boolean unique = false;

    private Object defaultValue;

    private String description;
}
