package com.asekulsk.nextver.api.request

import com.asekulsk.nextver.security.interfaces.IDataSecurity

class RegisterRequest implements IDataSecurity {
    String name
    String key

    @Override
    boolean Decipher(String key) {
        // TODO Implement me
        return false
    }
}
