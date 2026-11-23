package com.mockforge.runtime.compiler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockforge.project.domain.Project;
import com.mockforge.resource.domain.Resource;
import com.mockforge.resource.domain.ResourceField;
import com.mockforge.runtime.model.RuntimeSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Compiles a project and its resources into an executable RuntimeSnapshot.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SnapshotCompiler {

    private final ObjectMapper objectMapper;

    /**
     * Compile a project and its resources into a runtime snapshot.
     */
    public RuntimeSnapshot compile(Project project, List<Resource> resources) {
        log.info("Compiling snapshot for project: id={}, resources={}", 
                project.getId(), resources.size());

        List<RuntimeSnapshot.ResourceRuntime> runtimeResources = resources.stream()
                .map(this::compileResource)
                .collect(Collectors.toList());

        Map<String, RuntimeSnapshot.ResourceRuntime> resourcesBySlug = new HashMap<>();
        for (RuntimeSnapshot.ResourceRuntime res : runtimeResources) {
            resourcesBySlug.put(res.getSlug(), res);
        }

        RuntimeSnapshot snapshot = new RuntimeSnapshot(
                project.getId(),
                project.getName(),
                project.getBasePath(),
                project.getStorageMode(),
                project.getConfigVersion(),
                runtimeResources,
                resourcesBySlug
        );

        log.info("Compiled snapshot: version={}, resources={}, basePath={}", 
                snapshot.getConfigVersion(), snapshot.getResources().size(), snapshot.getBasePath());

        return snapshot;
    }

    /**
     * Compile a resource into runtime form.
     */
    private RuntimeSnapshot.ResourceRuntime compileResource(Resource resource) {
        List<RuntimeSnapshot.FieldRuntime> fields = resource.getFields().stream()
                .map(this::compileField)
                .collect(Collectors.toList());

        Map<String, RuntimeSnapshot.FieldRuntime> fieldsByName = new HashMap<>();
        for (RuntimeSnapshot.FieldRuntime field : fields) {
            fieldsByName.put(field.getName(), field);
        }

        return new RuntimeSnapshot.ResourceRuntime(
                resource.getId(),
                resource.getName(),
                resource.getSlug(),
                resource.getSchemaVersion(),
                resource.getTimestamps(),
                fields,
                fieldsByName
        );
    }

    /**
     * Compile a field into runtime form.
     */
    private RuntimeSnapshot.FieldRuntime compileField(ResourceField field) {
        Object defaultValue = null;
        if (field.getDefaultJson() != null) {
            try {
                defaultValue = objectMapper.readValue(field.getDefaultJson(), Object.class);
            } catch (JsonProcessingException e) {
                log.warn("Failed to parse default value for field {}: {}", 
                        field.getName(), e.getMessage());
            }
        }

        return new RuntimeSnapshot.FieldRuntime(
                field.getName(),
                field.getFieldType().name(),
                field.getRequired(),
                field.getUniqueFlag(),
                defaultValue,
                field.getPosition()
        );
    }
}
