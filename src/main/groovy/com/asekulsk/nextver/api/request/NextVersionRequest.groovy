package com.asekulsk.nextver.api.request

import com.asekulsk.nextver.domain.enumeration.VersionIncrementType

class NextVersionRequest {
    String versionName
    VersionIncrementType versionIncrementType
}
