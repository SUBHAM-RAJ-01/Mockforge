package com.mockforge.resource.dto;

import com.mockforge.resource.domain.Resource;
import com.mockforge.resource.domain.ResourceField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Response DTO for resource data.
 */
@Data
public class ResourceResponse {

    private String id;
    private String projectId;
    private String name;
    private String slug;
    private Long schemaVersion;
    private Boolean timestamps;
    private List<FieldResponse> fields;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ResourceResponse from(Resource resource) {
        ResourceResponse response = new ResourceResponse();
        response.setId(resource.getId());
        response.setProjectId(resource.getProjectId());
        response.setName(resource.getName());
        response.setSlug(resource.getSlug());
        response.setSchemaVersion(resource.getSchemaVersion());
        response.setTimestamps(resource.getTimestamps());
        response.setFields(resource.getFields().stream()
                .map(FieldResponse::from)
                .collect(Collectors.toList()));
        response.setCreatedAt(resource.getCreatedAt());
        response.setUpdatedAt(resource.getUpdatedAt());
        return response;
    }

    @Data
    public static class FieldResponse {
        private String id;
        private String name;
        private String type;
        private Boolean required;
        private Boolean unique;
        private String defaultValue;
        private Integer position;

        public static FieldResponse from(ResourceField field) {
            FieldResponse response = new FieldResponse();
            response.setId(field.getId());
            response.setName(field.getName());
            response.setType(field.getFieldType().name());
            response.setRequired(field.getRequired());
            response.setUnique(field.getUniqueFlag());
            response.setDefaultValue(field.getDefaultJson());
            response.setPosition(field.getPosition());
            return response;
        }
    }
}
