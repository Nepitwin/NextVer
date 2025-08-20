package com.asekulsk.nextver.domain.interfaces

import com.asekulsk.nextver.domain.enumeration.VersionIncrementType

interface IVersioningStrategy {
    String getNextVersion(String currentVersion, VersionIncrementType type)
    boolean validate(String version)
}
