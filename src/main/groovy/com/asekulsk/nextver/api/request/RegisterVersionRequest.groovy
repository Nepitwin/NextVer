package com.asekulsk.nextver.api.request

import com.asekulsk.nextver.domain.enumeration.VersionType

class RegisterVersionRequest {
    String version
    String versionName
    VersionType type
}
