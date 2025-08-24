package com.asekulsk.nextver.unit.api.request

import com.asekulsk.nextver.api.request.NextVersionRequest
import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import spock.lang.Specification

class NextVersionRequestSpec extends Specification {

    def "should allow setting and getting versionName and versionIncrementType"() {
        given:
        def request = new NextVersionRequest()

        when:
        request.versionName = "1.0.0"
        request.versionIncrementType = VersionIncrementType.MINOR

        then:
        request.versionName == "1.0.0"
        request.versionIncrementType == VersionIncrementType.MINOR
    }

    def "should support different VersionIncrementType values"() {
        when:
        def request = new NextVersionRequest(versionName: "2.0.0", versionIncrementType: type)

        then:
        request.versionIncrementType == type

        where:
        type << VersionIncrementType.values()
    }
}
