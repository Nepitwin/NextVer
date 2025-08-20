package com.asekulsk.nextver.persistence.repository

import com.asekulsk.nextver.persistence.model.Project
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name)
}
