package com.asekulsk.nextver.service

import com.asekulsk.nextver.enumeration.VersionIncrementType
import com.asekulsk.nextver.interfaces.VersioningStrategy
import com.asekulsk.nextver.persistence.Version
import com.asekulsk.nextver.repository.ProjectRepository
import com.asekulsk.nextver.repository.VersionRepository
import com.asekulsk.nextver.versioning.FourPartStrategy
import com.asekulsk.nextver.versioning.SemVerStrategy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VersionService {

    private final VersionRepository versionRepository
    private final ProjectRepository projectRepository

    private final Map<String, VersioningStrategy> strategies = [
            "SEMVER"    : new SemVerStrategy(),
            "FOUR_PART" : new FourPartStrategy()
    ] as Map<String, VersioningStrategy>

    VersionService(VersionRepository versionRepository, ProjectRepository projectRepository) {
        this.versionRepository = versionRepository
        this.projectRepository = projectRepository
    }

    @Transactional
    Version getNextVersion(String projectName, VersionIncrementType type) {
        def project = projectRepository.findByName(projectName)
                .orElseThrow { new IllegalArgumentException("Project not found: $projectName") }

        def strategy = strategies[project.versionFormat]
        if (!strategy) throw new IllegalArgumentException("Unsupported version format: ${project.versionFormat}")

        def current = versionRepository.findTopByProjectOrderByIdDesc(projectName)
                .map { it.versionString }
                .orElse(initialVersionFor(project.versionFormat))

        def next = strategy.getNextVersion(current, type)

        def saved = new Version(
                project: projectName,
                versionString: next,
                released: true
        )
        versionRepository.save(saved)
    }

    @Transactional
    Version registerVersion(String projectName, String versionString, boolean released = true) {
        def project = projectRepository.findByName(projectName)
                .orElseThrow { new IllegalArgumentException("Project not found: $projectName") }

        def strategy = strategies[project.versionFormat]
        if (!strategy) throw new IllegalArgumentException("Unsupported version format: ${project.versionFormat}")

        if (!strategy.validate(versionString)) {
            throw new IllegalArgumentException("Version $versionString does not match format ${project.versionFormat}")
        }

        def entity = new Version(project: projectName, versionString: versionString, released: released)
        versionRepository.save(entity)
    }

    private static String initialVersionFor(String format) {
        switch (format) {
            case "SEMVER": return "1.0.0"
            case "FOUR_PART": return "1.0.0.0"
            default: return "0.1.0"
        }
    }
}
