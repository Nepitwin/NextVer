package com.asekulsk.nextver.api.request

import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.security.interfaces.IDataSecurity

class RegisterVersionRequest implements IDataSecurity {
    String version
    String versionName
    VersionType type

    @Override
    boolean Decipher(String key) {
        // TODO Implement me
        return false
    }
}
