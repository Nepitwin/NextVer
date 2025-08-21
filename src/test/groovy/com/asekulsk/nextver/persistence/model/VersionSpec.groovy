package com.asekulsk.nextver.persistence.model

import com.asekulsk.nextver.domain.enumeration.VersionType
import spock.lang.Specification

class VersionSpec extends Specification {

    def "should allow setting and getting fields"() {
        given:
        def project = new Project(id: 1L, name: "NextVer")
        def version = new Version()

        when:
        version.id = 1L
        version.name = "Initial Release"
        version.type = VersionType.SEMVER
        version.version = "1.0.0"
        version.project = project

        then:
        version.id == 1L
        version.name == "Initial Release"
        version.type == VersionType.SEMVER
        version.version == "1.0.0"
        version.project == project
    }

    def "should support lazy project assignment"() {
        given:
        def version = new Version()

        expect:
        version.project == null

        when:
        def project = new Project(name: "NextVer")
        version.project = project

        then:
        version.project == project
    }
}
