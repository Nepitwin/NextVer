package com.asekulsk.nextver.versioning

import com.asekulsk.nextver.enumeration.VersionIncrementType
import com.asekulsk.nextver.interfaces.IVersioningStrategy

class FourPartStrategy implements IVersioningStrategy {

    @Override
    String getNextVersion(String currentVersion, VersionIncrementType type) {
        def parts = currentVersion.split("\\.")

        if (parts.size() != 4) {
            throw new IllegalArgumentException("Invalid four-part version: $currentVersion")
        }

        int major = parts[0] as int
        int minor = parts[1] as int
        int patch = parts[2] as int
        int build = parts[3] as int

        switch (type) {
            case VersionIncrementType.MAJOR:
                major++; minor = 0; patch = 0; build = 0
                break
            case VersionIncrementType.MINOR:
                minor++; patch = 0; build = 0
                break
            case VersionIncrementType.PATCH:
                patch++; build = 0
                break
            case VersionIncrementType.BUILD:
                build++
                break
        }

        return "$major.$minor.$patch.$build"
    }

    @Override
    boolean validate(String version) {
        version ==~ /\d+\.\d+\.\d+\.\d+/
    }
}
