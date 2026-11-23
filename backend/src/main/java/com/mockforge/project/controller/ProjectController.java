package com.mockforge.project.controller;

import com.mockforge.project.domain.Project;
import com.mockforge.project.dto.CreateProjectRequest;
import com.mockforge.project.dto.ProjectResponse;
import com.mockforge.project.dto.UpdateProjectRequest;
import com.mockforge.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for project management.
 * Base path: /_mockforge/v1/projects
 */
@RestController
@RequestMapping("/_mockforge/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Create a new project.
     * POST /_mockforge/v1/projects
     */
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest request) {
        Project project = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProjectResponse.from(project));
    }

    /**
     * Get all projects.
     * GET /_mockforge/v1/projects
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> projects = projectService.getAllProjects().stream()
                .map(ProjectResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    /**
     * Get a project by ID.
     * GET /_mockforge/v1/projects/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable String id) {
        return projectService.getProjectById(id)
                .map(ProjectResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update a project.
     * PUT /_mockforge/v1/projects/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable String id,
            @Valid @RequestBody UpdateProjectRequest request) {
        try {
            Project project = projectService.updateProject(id, request);
            return ResponseEntity.ok(ProjectResponse.from(project));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Partially update a project.
     * PATCH /_mockforge/v1/projects/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> patchProject(
            @PathVariable String id,
            @RequestBody UpdateProjectRequest request) {
        try {
            Project project = projectService.updateProject(id, request);
            return ResponseEntity.ok(ProjectResponse.from(project));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a project.
     * DELETE /_mockforge/v1/projects/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
