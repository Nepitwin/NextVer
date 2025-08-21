package com.asekulsk.nextver.persistence.service

import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.persistence.interfaces.IProjectService
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.repository.ProjectRepository
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
                .ifPresent {throw new NextVerException("Project already exists by name '$name'", NextVerReason.DataAlreadyExists) }

        Project project = new Project()
        project.name = name

        projectRepository.save(project) != null
    }

    @Override
    Project getProjectByName(String name) {
        return projectRepository.findByName(name).orElseThrow{throw new NextVerException("Project not found by name '$name'", NextVerReason.DataNotFound)}
    }
}
