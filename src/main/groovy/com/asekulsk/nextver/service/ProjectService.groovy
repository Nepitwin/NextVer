package com.asekulsk.nextver.service

import com.asekulsk.nextver.exceptions.DataNotFoundException
import com.asekulsk.nextver.exceptions.ProjectAlreadyExistsException
import com.asekulsk.nextver.interfaces.IProjectService
import com.asekulsk.nextver.persistence.Project
import com.asekulsk.nextver.repository.ProjectRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository

    ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository
    }

    @Override
    @Transactional
    boolean register(String name) {
        projectRepository.findByName(name)
                .ifPresent {throw new ProjectAlreadyExistsException("Project already exists by name '$name'") }

        Project project = new Project()
        project.name = name

        projectRepository.save(project) != null
    }

    @Override
    Project getProjectByName(String name) {
        return projectRepository.findByName(name).orElseThrow{throw new DataNotFoundException("Project not found by name '$name'")}
    }
}
