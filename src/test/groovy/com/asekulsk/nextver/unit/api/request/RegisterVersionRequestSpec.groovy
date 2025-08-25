package com.asekulsk.nextver.unit.api.request

import com.asekulsk.nextver.api.request.RegisterVersionRequest
import com.asekulsk.nextver.domain.enumeration.VersionType
import spock.lang.Specification

class RegisterVersionRequestSpec extends Specification {

    def "should allow setting and getting version, versionName and type"() {
        given:
        def request = new RegisterVersionRequest()

        when:
        request.version = "1.0.0"
        request.versionName = "Initial Release"
        request.type = VersionType.SEMVER

        then:
        request.version == "1.0.0"
        request.versionName == "Initial Release"
        request.type == VersionType.SEMVER
    }

    def "should support different VersionType values"() {
        when:
        def request = new RegisterVersionRequest(
                version: "2.0.0",
                versionName: "Next Release",
                type: type as VersionType
        )

        then:
        request.type == type

        where:
        type << VersionType.values()
    }
}
