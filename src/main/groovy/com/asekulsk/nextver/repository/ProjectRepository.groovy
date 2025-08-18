package com.asekulsk.nextver.repository

import com.asekulsk.nextver.persistence.Project
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name)
}
