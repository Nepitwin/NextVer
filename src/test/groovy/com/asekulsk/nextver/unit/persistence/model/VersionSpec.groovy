package com.asekulsk.nextver.unit.persistence.model

import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.persistence.model.Version
import spock.lang.Specification

class VersionSpec extends Specification {

    def "should allow setting and getting fields"() {
        given:
        def version = new Version()

        when:
        version.id = 1L
        version.name = "Initial Release"
        version.type = VersionType.SEMVER
        version.version = "1.0.0"

        then:
        version.id == 1L
        version.name == "Initial Release"
        version.type == VersionType.SEMVER
        version.version == "1.0.0"
    }

    def "tuple constructor from @Canonical should assign all fields in order"() {
        when:
        def version = new Version(5L, "Initial Release", VersionType.SEMVER, "1.0.0")

        then:
        version.id == 5L
        version.name == "Initial Release"
        version.type == VersionType.SEMVER
        version.version == "1.0.0"
    }

    def "map constructor should set provided fields"() {
        when:
        def version = new Version(name: "Hotfix", type: VersionType.SEMVER, version: "1.0.1")

        then:
        version.id == null
        version.name == "Hotfix"
        version.type == VersionType.SEMVER
        version.version == "1.0.1"
    }

    def "equals and hashCode should consider all fields"() {
        given:
        def v1 = new Version(10L, "Initial", VersionType.SEMVER, "1.0.0")
        def v2 = new Version(10L, "Initial", VersionType.SEMVER, "1.0.0",)

        expect:
        v1 == v2
        v1.hashCode() == v2.hashCode()

        when:
        v2.version = "1.0.1"

        then:
        v1 != v2
        v1.hashCode() != v2.hashCode()
    }

    def "toString should contain key field values"() {
        given:
        def version = new Version(11L, "Initial Release", VersionType.SEMVER, "1.0.0")

        when:
        def s = version.toString()

        then:
        s.contains("Initial Release")
        s.contains("1.0.0")
        s.contains("SEMVER")
    }
}
