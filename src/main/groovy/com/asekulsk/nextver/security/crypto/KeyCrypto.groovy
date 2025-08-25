package com.asekulsk.nextver.security.crypto

import org.bouncycastle.crypto.util.OpenSSHPublicKeyUtil

class KeyCrypto {
    private static def validAlgorithms = ['ssh-rsa', 'ssh-dss', 'ssh-ed25519', 'ecdsa-sha2-nistp256', 'ecdsa-sha2-nistp384', 'ecdsa-sha2-nistp521']

    static boolean IsValidSshKey(String key) {
        if (!key) {
            return false
        }

        def parts = key.trim().split(/\s+/)

        if (parts.length < 2) {
            return false
        }

        def algorithm = parts[0]
        def keyData = parts[1]

        if (!(algorithm in validAlgorithms)) {
            return false
        }

        try {
            return OpenSSHPublicKeyUtil.parsePublicKey(Base64.getDecoder().decode(keyData)) != null
        } catch (Exception ignored) {
            return false
        }
    }
}
