package com.mockforge.resource.controller;

import com.mockforge.resource.domain.Resource;
import com.mockforge.resource.dto.ResourceResponse;
import com.mockforge.resource.dto.UpdateResourceRequest;
import com.mockforge.resource.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for resource operations by ID.
 * Base path: /_mockforge/v1/resources/{id}
 */
@RestController
@RequestMapping("/_mockforge/v1/resources")
@RequiredArgsConstructor
public class ResourceByIdController {

    private final ResourceService resourceService;

    /**
     * Get a resource by ID.
     * GET /_mockforge/v1/resources/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponse> getResource(@PathVariable String id) {
        return resourceService.getResourceById(id)
                .map(ResourceResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update a resource.
     * PUT /_mockforge/v1/resources/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponse> updateResource(
            @PathVariable String id,
            @Valid @RequestBody UpdateResourceRequest request) {
        try {
            Resource resource = resourceService.updateResource(id, request);
            return ResponseEntity.ok(ResourceResponse.from(resource));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a resource.
     * DELETE /_mockforge/v1/resources/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable String id) {
        try {
            resourceService.deleteResource(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
