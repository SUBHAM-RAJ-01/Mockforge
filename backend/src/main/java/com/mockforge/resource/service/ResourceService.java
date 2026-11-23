package com.mockforge.resource.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockforge.common.IdGenerator;
import com.mockforge.project.repository.ProjectRepository;
import com.mockforge.resource.domain.Resource;
import com.mockforge.resource.domain.ResourceField;
import com.mockforge.resource.dto.CreateResourceRequest;
import com.mockforge.resource.dto.FieldDefinition;
import com.mockforge.resource.dto.UpdateResourceRequest;
import com.mockforge.resource.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for managing resources and their schemas.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;
    private final ObjectMapper objectMapper;

    /**
     * Create a new resource for a project.
     */
    @Transactional
    public Resource createResource(String projectId, CreateResourceRequest request) {
        // Verify project exists
        if (!projectRepository.existsById(projectId)) {
            throw new IllegalArgumentException("Project not found: " + projectId);
        }

        String slug = generateSlug(request.getName());

        // Check if slug already exists for this project
        if (resourceRepository.existsByProjectIdAndSlug(projectId, slug)) {
            throw new IllegalArgumentException("Resource with similar name already exists in this project: " + slug);
        }

        // Validate fields
        validateFields(request.getFields());

        Resource resource = new Resource();
        resource.setId(IdGenerator.generateResourceId());
        resource.setProjectId(projectId);
        resource.setName(request.getName());
        resource.setSlug(slug);
        resource.setTimestamps(request.getTimestamps() != null ? request.getTimestamps() : true);

        // Add fields
        int position = 0;
        for (FieldDefinition fieldDef : request.getFields()) {
            ResourceField field = createFieldFromDefinition(fieldDef, position++);
            resource.addField(field);
        }

        Resource saved = resourceRepository.save(resource);
        log.info("Created resource: id={}, projectId={}, slug={}, fields={}", 
                saved.getId(), projectId, slug, saved.getFields().size());
        return saved;
    }

    /**
     * Get all resources for a project.
     */
    @Transactional(readOnly = true)
    public List<Resource> getResourcesByProjectId(String projectId) {
        return resourceRepository.findByProjectId(projectId);
    }

    /**
     * Get a resource by ID.
     */
    @Transactional(readOnly = true)
    public Optional<Resource> getResourceById(String id) {
        return resourceRepository.findById(id);
    }

    /**
     * Get a resource by project ID and slug.
     */
    @Transactional(readOnly = true)
    public Optional<Resource> getResourceBySlug(String projectId, String slug) {
        return resourceRepository.findByProjectIdAndSlug(projectId, slug);
    }

    /**
     * Update a resource.
     */
    @Transactional
    public Resource updateResource(String id, UpdateResourceRequest request) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + id));

        boolean changed = false;

        if (request.getName() != null && !request.getName().equals(resource.getName())) {
            String newSlug = generateSlug(request.getName());
            if (!newSlug.equals(resource.getSlug()) && 
                resourceRepository.existsByProjectIdAndSlug(resource.getProjectId(), newSlug)) {
                throw new IllegalArgumentException("Resource with similar name already exists in this project: " + newSlug);
            }
            resource.setName(request.getName());
            resource.setSlug(newSlug);
            changed = true;
        }

        if (request.getTimestamps() != null && !request.getTimestamps().equals(resource.getTimestamps())) {
            resource.setTimestamps(request.getTimestamps());
            changed = true;
        }

        if (request.getFields() != null) {
            validateFields(request.getFields());
            
            // Remove old fields
            resource.getFields().clear();

            // Add new fields
            int position = 0;
            for (FieldDefinition fieldDef : request.getFields()) {
                ResourceField field = createFieldFromDefinition(fieldDef, position++);
                resource.addField(field);
            }
            changed = true;
        }

        if (changed) {
            resource.incrementSchemaVersion();
            Resource updated = resourceRepository.save(resource);
            log.info("Updated resource: id={}, version={}, fields={}", 
                    updated.getId(), updated.getSchemaVersion(), updated.getFields().size());
            return updated;
        }

        return resource;
    }

    /**
     * Delete a resource.
     */
    @Transactional
    public void deleteResource(String id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found: " + id));

        resourceRepository.delete(resource);
        log.info("Deleted resource: id={}, slug={}", id, resource.getSlug());
    }

    /**
     * Validate field definitions.
     */
    private void validateFields(List<FieldDefinition> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("At least one field is required");
        }

        // Check for duplicate field names
        Set<String> names = fields.stream()
                .map(FieldDefinition::getName)
                .collect(Collectors.toSet());
        
        if (names.size() != fields.size()) {
            throw new IllegalArgumentException("Duplicate field names found");
        }

        // Validate reserved field names
        Set<String> reservedNames = Set.of("id", "createdAt", "updatedAt");
        for (String name : names) {
            if (reservedNames.contains(name)) {
                throw new IllegalArgumentException("Field name '" + name + "' is reserved");
            }
        }
    }

    /**
     * Create a ResourceField from a FieldDefinition.
     */
    private ResourceField createFieldFromDefinition(FieldDefinition def, int position) {
        ResourceField field = new ResourceField();
        field.setId(IdGenerator.generateFieldId());
        field.setName(def.getName());
        field.setFieldType(def.getType());
        field.setRequired(def.getRequired() != null ? def.getRequired() : false);
        field.setUniqueFlag(def.getUnique() != null ? def.getUnique() : false);
        field.setPosition(position);

        // Serialize default value as JSON
        if (def.getDefaultValue() != null) {
            try {
                field.setDefaultJson(objectMapper.writeValueAsString(def.getDefaultValue()));
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Invalid default value for field: " + def.getName(), e);
            }
        }

        // Store description in config JSON
        if (def.getDescription() != null) {
            try {
                field.setConfigJson(objectMapper.writeValueAsString(
                        java.util.Map.of("description", def.getDescription())
                ));
            } catch (JsonProcessingException e) {
                log.warn("Failed to serialize field config: {}", e.getMessage());
            }
        }

        return field;
    }

    /**
     * Generate a URL-safe slug from a resource name.
     */
    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
