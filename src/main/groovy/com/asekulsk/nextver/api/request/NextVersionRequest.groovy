package com.asekulsk.nextver.api.request

import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.security.interfaces.IDataSecurity

class NextVersionRequest implements IDataSecurity {
    String versionName
    VersionIncrementType versionIncrementType

    @Override
    boolean Decipher(String key) {
        // TODO Implement me
        return false
    }
}
