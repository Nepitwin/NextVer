package com.asekulsk.nextver.persistence.service

import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.domain.interfaces.IVersioningStrategy
import com.asekulsk.nextver.domain.versioning.FourPartStrategy
import com.asekulsk.nextver.domain.versioning.SemanticVersionStrategy
import com.asekulsk.nextver.persistence.interfaces.IVersionService
import com.asekulsk.nextver.persistence.model.Version
import com.asekulsk.nextver.persistence.repository.ProjectRepository
import com.asekulsk.nextver.persistence.repository.VersionRepository
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
                .orElseThrow { new NextVerException("Project not found from name '$projectName'", NextVerReason.DataNotFound) }

        var version = project.versions.find { it -> it.name == versionName}

        if (version == null)
        {
            throw new NextVerException("Version not found by name '$versionName'", NextVerReason.DataNotFound)
        }

        def strategy = strategies[version.type]
        version.version = strategy.getNextVersion(version.version, incrementType)

        versionRepository.save(version)
    }
}
