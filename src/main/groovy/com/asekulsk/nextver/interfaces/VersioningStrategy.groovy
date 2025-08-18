package com.asekulsk.nextver.interfaces

import com.asekulsk.nextver.enumeration.VersionIncrementType

interface VersioningStrategy {
    String getNextVersion(String currentVersion, VersionIncrementType type)
    boolean validate(String version)
}
