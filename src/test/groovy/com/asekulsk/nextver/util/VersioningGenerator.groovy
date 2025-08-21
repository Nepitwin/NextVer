package com.asekulsk.nextver.util

import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.model.Version

class VersioningGenerator {
    static def versionFactories = [
            { p -> createSemverVersion(p) },
            { p -> createFourPartVersion(p) }
    ]

    static Version createSemverVersion(Project project) {
        PersistenceGenerator.GenerateVersion(
                PersistenceGenerator.GenerateRandomString(20),
                project,
                "1.0.0",
                VersionType.SEMVER
        )
    }

    static Version createFourPartVersion(Project project) {
        PersistenceGenerator.GenerateVersion(
                PersistenceGenerator.GenerateRandomString(20),
                project,
                "1.0.0.0",
                VersionType.FOUR_PART
        )
    }
}
