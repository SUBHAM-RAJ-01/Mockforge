package com.mockforge.project.service;

import com.mockforge.common.IdGenerator;
import com.mockforge.project.domain.Project;
import com.mockforge.project.domain.ProjectStatus;
import com.mockforge.project.dto.CreateProjectRequest;
import com.mockforge.project.dto.UpdateProjectRequest;
import com.mockforge.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing projects.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;

    /**
     * Create a new project.
     */
    @Transactional
    public Project createProject(CreateProjectRequest request) {
        String slug = generateSlug(request.getName());
        
        // Check if slug already exists
        if (projectRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Project with similar name already exists: " + slug);
        }

        Project project = new Project();
        project.setId(IdGenerator.generateProjectId());
        project.setName(request.getName());
        project.setSlug(slug);
        project.setBasePath(request.getBasePath() != null ? request.getBasePath() : "/api");
        project.setStorageMode(request.getStorageMode());
        project.setStatus(ProjectStatus.STOPPED);
        project.setDescription(request.getDescription());

        Project saved = projectRepository.save(project);
        log.info("Created project: id={}, slug={}", saved.getId(), saved.getSlug());
        return saved;
    }

    /**
     * Get all projects.
     */
    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Get project by ID.
     */
    @Transactional(readOnly = true)
    public Optional<Project> getProjectById(String id) {
        return projectRepository.findById(id);
    }

    /**
     * Get project by slug.
     */
    @Transactional(readOnly = true)
    public Optional<Project> getProjectBySlug(String slug) {
        return projectRepository.findBySlug(slug);
    }

    /**
     * Update an existing project.
     */
    @Transactional
    public Project updateProject(String id, UpdateProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));

        boolean changed = false;

        if (request.getName() != null && !request.getName().equals(project.getName())) {
            String newSlug = generateSlug(request.getName());
            if (!newSlug.equals(project.getSlug()) && projectRepository.existsBySlug(newSlug)) {
                throw new IllegalArgumentException("Project with similar name already exists: " + newSlug);
            }
            project.setName(request.getName());
            project.setSlug(newSlug);
            changed = true;
        }

        if (request.getBasePath() != null && !request.getBasePath().equals(project.getBasePath())) {
            project.setBasePath(request.getBasePath());
            changed = true;
        }

        if (request.getStorageMode() != null && !request.getStorageMode().equals(project.getStorageMode())) {
            project.setStorageMode(request.getStorageMode());
            changed = true;
        }

        if (request.getDescription() != null && !request.getDescription().equals(project.getDescription())) {
            project.setDescription(request.getDescription());
            changed = true;
        }

        if (changed) {
            project.incrementConfigVersion();
            Project updated = projectRepository.save(project);
            log.info("Updated project: id={}, version={}", updated.getId(), updated.getConfigVersion());
            return updated;
        }

        return project;
    }

    /**
     * Delete a project.
     */
    @Transactional
    public void deleteProject(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));

        projectRepository.delete(project);
        log.info("Deleted project: id={}, slug={}", id, project.getSlug());
    }

    /**
     * Generate a URL-safe slug from a project name.
     */
    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
