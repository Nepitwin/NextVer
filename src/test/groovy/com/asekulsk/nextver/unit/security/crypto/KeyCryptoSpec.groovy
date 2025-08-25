package com.asekulsk.nextver.unit.security.crypto

import com.asekulsk.nextver.security.crypto.KeyCrypto
import com.asekulsk.nextver.util.CryptoData
import spock.lang.Specification
import spock.lang.Unroll

class KeyCryptoSpec extends Specification {

    @Unroll
    def "should validate correct keys (#desc)"() {
        expect:
        KeyCrypto.IsValidSshKey(key)

        where:
        key                                | desc
        CryptoData.ValidRsaKey             | "ValidRsaKey"
        CryptoData.ValidSecondRsaKey       | "ValidSecondRsaKey"
        CryptoData.ValidECdsaSha2Nistp256  | "ValidECdsaSha2Nistp256"
        CryptoData.ValidEcdsaSha2Nistp384  | "ValidEcdsaSha2Nistp384"
        CryptoData.ValidECdsaSha2Nistp521  | "ValidECdsaSha2Nistp521"
        CryptoData.ValidEd25519            | "ValidEd25519"
        CryptoData.ValidDssKey             | "ValidDssKey"
    }

    def "should return false for null or empty key"() {
        expect:
        !KeyCrypto.IsValidSshKey(null)
        !KeyCrypto.IsValidSshKey("")
        !KeyCrypto.IsValidSshKey("   ")
    }

    def "should return false for unsupported algorithm"() {
        given:
        def invalidAlgoKey = "ssh-rss ${CryptoData.ValidRsaKey.split(' ')[1]}"

        expect:
        !KeyCrypto.IsValidSshKey(invalidAlgoKey)
    }

    def "should return false for key without enough parts"() {
        expect:
        !KeyCrypto.IsValidSshKey("ssh-rsa-only")
    }

    def "should return false for corrupted base64 data"() {
        given:
        def corruptedKey = "ssh-rsa NOT_BASE64_AT_ALL"

        expect:
        !KeyCrypto.IsValidSshKey(corruptedKey)
    }

    def "should return false if base64 decodes but key is not parsable"() {
        given:
        def fakeKeyData = Base64.encoder.encodeToString("not-a-valid-key".bytes)
        def fakeKey = "ssh-rsa ${fakeKeyData}"

        expect:
        !KeyCrypto.IsValidSshKey(fakeKey)
    }
}
