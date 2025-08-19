package com.asekulsk.nextver.interfaces

import com.asekulsk.nextver.enumeration.VersionType
import com.asekulsk.nextver.persistence.Version

interface IVersionService {
    boolean register(String projectName, String versionName, String version, VersionType type);
    Version getNextVersion(String projectName, String versionName);
}