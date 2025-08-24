package com.asekulsk.nextver.util

import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.model.Version

class VersioningGenerator {
    static def versionFactories = [
            { createSemverVersion() },
            { createFourPartVersion() }
    ]

    static Version createSemverVersion() {
        PersistenceGenerator.GenerateVersion(
                version: "1.0.0",
                type: VersionType.SEMVER
        )
    }

    static Version createFourPartVersion() {
        PersistenceGenerator.GenerateVersion(
                version: "1.0.0.0",
                type: VersionType.FOUR_PART
        )
    }
}
