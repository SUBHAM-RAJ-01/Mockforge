package com.mockforge.project.repository;

import com.mockforge.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Project entity operations.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    /**
     * Find a project by its slug.
     * 
     * @param slug The project slug
     * @return Optional containing the project if found
     */
    Optional<Project> findBySlug(String slug);

    /**
     * Check if a project with the given slug exists.
     * 
     * @param slug The project slug
     * @return true if exists, false otherwise
     */
    boolean existsBySlug(String slug);
}
