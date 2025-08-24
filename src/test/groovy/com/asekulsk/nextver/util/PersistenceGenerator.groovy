package com.asekulsk.nextver.util

import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.model.Version

class PersistenceGenerator {
    static Project GenerateProject(Map args = [:]) {
        def name     = args.name ?: StringUtil.GenerateRandomString(20)
        def key      = args.key ?: CryptoData.ValidRsaKey
        def versions = args.versions as List<Version> ?: [] as List<Version>

        return new Project(name: name, key: key, versions: versions)
    }

    static Version GenerateVersion(Map args = [:]) {
        def name     = args.name ?: "Initial Release"
        def type     = args.type as VersionType ?: VersionType.SEMVER
        def version  = args.version ?: "1.0.0"

        return new Version(name: name, type: type, version: version)
    }
}
