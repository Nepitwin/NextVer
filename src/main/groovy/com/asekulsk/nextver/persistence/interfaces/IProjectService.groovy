package com.asekulsk.nextver.persistence.interfaces

import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.model.Version

interface IProjectService {
    boolean register(String name, String key)
    boolean register(String projectName, String versionName, String version, VersionType type);
    Project getProjectByName(String name)
    Version getNextVersion(String projectName, String versionName, VersionIncrementType type)
}