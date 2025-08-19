package com.asekulsk.nextver.interfaces

import com.asekulsk.nextver.enumeration.VersionIncrementType

interface IVersioningStrategy {
    String getNextVersion(String currentVersion, VersionIncrementType type)
    boolean validate(String version)
}
