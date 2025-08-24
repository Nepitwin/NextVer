package com.asekulsk.nextver.persistence.service

import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.domain.interfaces.IVersioningStrategy
import com.asekulsk.nextver.domain.versioning.FourPartStrategy
import com.asekulsk.nextver.domain.versioning.SemanticVersionStrategy
import com.asekulsk.nextver.persistence.interfaces.IProjectService
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.model.Version
import com.asekulsk.nextver.persistence.repository.ProjectRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository

    private final Map<VersionType, IVersioningStrategy> strategies = [
            (VersionType.SEMVER)   : new SemanticVersionStrategy(),
            (VersionType.FOUR_PART): new FourPartStrategy()
    ] as Map<VersionType, IVersioningStrategy>

    ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository
    }

    @Override
    @Transactional
    boolean register(String name, String key) {
        projectRepository.findByName(name)
                .ifPresent {throw new NextVerException("Project already exists by name '$name'", NextVerReason.DataAlreadyExists) }
        try
        {
            Project project = new Project(name: name, key: key)
            projectRepository.save(project) != null
        }
        catch (IllegalArgumentException ex)
        {
            throw new NextVerException("Failed to register project '$name'", NextVerReason.InvalidData, ex)
        }
    }

    @Override
    boolean register(String projectName, String versionName, String version, VersionType type) {
        def project = projectRepository.findByName(projectName)
                .orElseThrow { new NextVerException("Project not found from name '$projectName'", NextVerReason.DataNotFound) }

        def strategy = strategies[type]

        if (!strategy) {
            throw new NextVerException("Unsupported version format: ${type}", NextVerReason.InvalidData)
        }

        if (!strategy.validate(version)) {
            throw new NextVerException("Version $version does not match format", NextVerReason.InvalidData)
        }

        var projectVersion = project.versions.find { it -> it.name == versionName}

        if (projectVersion != null)
        {
            throw new NextVerException("Version found by name '$versionName'", NextVerReason.DataAlreadyExists)
        }

        projectRepository.save(project.addVersion(new Version(name: versionName, version: version, type: type))) != null
    }

    @Override
    Project getProjectByName(String name) {
        return projectRepository.findByName(name).orElseThrow{throw new NextVerException("Project not found by name '$name'", NextVerReason.DataNotFound)}
    }

    @Override
    Version getNextVersion(String projectName, String versionName, VersionIncrementType type) {
        def project = projectRepository.findByName(projectName)
                .orElseThrow { new NextVerException("Project not found from name '$projectName'", NextVerReason.DataNotFound) }

        var version = project.versions.find { it -> it.name == versionName}

        if (version == null)
        {
            throw new NextVerException("Version not found by name '$versionName'", NextVerReason.DataNotFound)
        }

        def strategy = strategies[version.type]
        version.version = strategy.getNextVersion(version.version, type)

        if (projectRepository.save(project) == null)
        {
            // TODO Implement me
            throw new NextVerException("Version not persist to project '$versionName'", NextVerReason.DataNotFound)
        }

        version
    }
}
