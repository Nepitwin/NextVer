package com.asekulsk.nextver.service

import com.asekulsk.nextver.enumeration.VersionIncrementType
import com.asekulsk.nextver.enumeration.VersionType
import com.asekulsk.nextver.exceptions.DataNotFoundException
import com.asekulsk.nextver.exceptions.InvalidDataException
import com.asekulsk.nextver.interfaces.IVersionService
import com.asekulsk.nextver.interfaces.IVersioningStrategy
import com.asekulsk.nextver.persistence.Version
import com.asekulsk.nextver.repository.ProjectRepository
import com.asekulsk.nextver.repository.VersionRepository
import com.asekulsk.nextver.versioning.FourPartStrategy
import com.asekulsk.nextver.versioning.SemanticVersionStrategy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VersionService implements IVersionService {

    private final VersionRepository versionRepository
    private final ProjectRepository projectRepository

    private final Map<VersionType, IVersioningStrategy> strategies = [
            (VersionType.SEMVER)   : new SemanticVersionStrategy(),
            (VersionType.FOUR_PART): new FourPartStrategy()
    ] as Map<VersionType, IVersioningStrategy>

    VersionService(VersionRepository versionRepository, ProjectRepository projectRepository) {
        this.versionRepository = versionRepository
        this.projectRepository = projectRepository
    }

    @Override
    @Transactional
    boolean register(String projectName, String versionName, String version, VersionType type) {

        def project = projectRepository.findByName(projectName)
                .orElseThrow { new DataNotFoundException("Project not found from name '$projectName'") }

        def strategy = strategies[type]

        if (!strategy) {
            throw new DataNotFoundException("Unsupported version format: ${type}")
        }

        if (!strategy.validate(version)) {
            throw new InvalidDataException("Version $version does not match format")
        }

        // TODO Verify if version name already exists in project

        def entity = new Version()
        entity.name = versionName
        entity.version = version
        entity.project = project
        entity.type = type

        versionRepository.save(entity) != null
    }

    @Override
    @Transactional
    Version getNextVersion(String projectName, String versionName, VersionIncrementType incrementType) {

        def project = projectRepository.findByName(projectName)
                .orElseThrow { new DataNotFoundException("Project not found from name '$projectName'") }

        var version = project.versions.find { it -> it.name == versionName}

        if (version == null)
        {
            throw new DataNotFoundException("Version not found by name '$versionName'")
        }

        def strategy = strategies[version.type]
        version.version = strategy.getNextVersion(version.version, incrementType)

        versionRepository.save(version)
    }
}
