package com.mockforge.resource.controller;

import com.mockforge.resource.domain.Resource;
import com.mockforge.resource.dto.CreateResourceRequest;
import com.mockforge.resource.dto.ResourceResponse;
import com.mockforge.resource.dto.UpdateResourceRequest;
import com.mockforge.resource.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for resource schema management.
 * Base path: /_mockforge/v1/projects/{projectId}/resources
 */
@RestController
@RequestMapping("/_mockforge/v1/projects/{projectId}/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    /**
     * Create a new resource for a project.
     * POST /_mockforge/v1/projects/{projectId}/resources
     */
    @PostMapping
    public ResponseEntity<ResourceResponse> createResource(
            @PathVariable String projectId,
            @Valid @RequestBody CreateResourceRequest request) {
        try {
            Resource resource = resourceService.createResource(projectId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResourceResponse.from(resource));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all resources for a project.
     * GET /_mockforge/v1/projects/{projectId}/resources
     */
    @GetMapping
    public ResponseEntity<List<ResourceResponse>> getResources(@PathVariable String projectId) {
        List<ResourceResponse> resources = resourceService.getResourcesByProjectId(projectId).stream()
                .map(ResourceResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    /**
     * Get a specific resource by slug.
     * GET /_mockforge/v1/projects/{projectId}/resources/{slug}
     */
    @GetMapping("/{slug}")
    public ResponseEntity<ResourceResponse> getResource(
            @PathVariable String projectId,
            @PathVariable String slug) {
        return resourceService.getResourceBySlug(projectId, slug)
                .map(ResourceResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
