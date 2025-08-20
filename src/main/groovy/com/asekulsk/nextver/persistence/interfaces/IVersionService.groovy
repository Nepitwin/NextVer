package com.asekulsk.nextver.persistence.interfaces

import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.persistence.model.Version

interface IVersionService {
    boolean register(String projectName, String versionName, String version, VersionType type);
    Version getNextVersion(String projectName, String versionName, VersionIncrementType incrementType);
}