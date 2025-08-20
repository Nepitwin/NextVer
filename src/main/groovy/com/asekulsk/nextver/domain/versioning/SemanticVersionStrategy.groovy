package com.asekulsk.nextver.domain.versioning

import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.domain.interfaces.IVersioningStrategy

class SemanticVersionStrategy implements IVersioningStrategy {

    @Override
    String getNextVersion(String currentVersion, VersionIncrementType type) {
        def parts = currentVersion.split("\\.")

        if (parts.size() != 3) {
            throw new IllegalArgumentException("Invalid semver: $currentVersion")
        }

        int major = parts[0] as int
        int minor = parts[1] as int
        int patch = parts[2] as int

        switch (type) {
            case VersionIncrementType.MAJOR:
                major++; minor = 0; patch = 0
                break
            case VersionIncrementType.MINOR:
                minor++; patch = 0
                break
            case VersionIncrementType.PATCH:
                patch++
                break
            case VersionIncrementType.BUILD:
                throw new IllegalArgumentException("BUILD not supported for SEMVER")
        }

        return "$major.$minor.$patch"
    }

    @Override
    boolean validate(String version) {
        version ==~ /\d+\.\d+\.\d+/
    }
}