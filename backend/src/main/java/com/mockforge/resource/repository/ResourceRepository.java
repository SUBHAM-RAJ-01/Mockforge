package com.mockforge.resource.repository;

import com.mockforge.resource.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Resource entity operations.
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {

    /**
     * Find all resources for a project.
     */
    List<Resource> findByProjectId(String projectId);

    /**
     * Find a resource by project ID and slug.
     */
    Optional<Resource> findByProjectIdAndSlug(String projectId, String slug);

    /**
     * Check if a resource exists for a project with the given slug.
     */
    boolean existsByProjectIdAndSlug(String projectId, String slug);

    /**
     * Delete all resources for a project.
     */
    void deleteByProjectId(String projectId);
}
