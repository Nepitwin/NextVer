package com.asekulsk.nextver.unit.persistence.model

import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.model.Version
import com.asekulsk.nextver.util.CryptoData
import spock.lang.Specification

class ProjectSpec extends Specification {

    def "should allow setting and getting fields"() {
        given:
        def project = new Project()

        when:
        project.id = 1L
        project.name = "NextVer"
        project.key = CryptoData.ValidRsaKey

        then:
        project.id == 1L
        project.name == "NextVer"
        project.key.startsWith("ssh-rsa")
    }

    def "should initialize versions as empty list"() {
        when:
        def project = new Project()

        then:
        project.versions != null
        project.versions.isEmpty()
    }

    def "should support all-args map constructor"() {
        when:
        def project = new Project(name: "NextVer", key: CryptoData.ValidRsaKey)

        then:
        project.name == "NextVer"
        project.key.startsWith("ssh-rsa")
        project.versions != null
        project.versions.isEmpty()
    }

    def "should compare projects using @Canonical generated equals"() {
        expect:
        (project1 == project2) == expected

        where:
        project1                                                  | project2                                                        || expected
        new Project(name: "NextVer", key: CryptoData.ValidRsaKey) | new Project(name: "NextVer", key: CryptoData.ValidRsaKey)       || true
        new Project(name: "NextVer", key: CryptoData.ValidRsaKey) | new Project(name: "Other", key: CryptoData.ValidRsaKey)         || false
        new Project(name: "NextVer", key: CryptoData.ValidRsaKey) | new Project(name: "NextVer", key: CryptoData.ValidSecondRsaKey) || false
    }

    def "addVersion should set back-reference and avoid duplicates"() {
        given:
        def project = new Project(name: "NextVer", key: CryptoData.ValidRsaKey)
        def v = new Version(id: 1L, name: "1.0.0")

        when:
        project.addVersion(v)

        then:
        project.versions.size() == 1
        project.versions[0].is(v)

        when: "adding same instance again"
        project.addVersion(v)

        then: "still only one"
        project.versions.size() == 1
    }

    def "removeVersion should detach back-reference"() {
        given:
        def project = new Project(name: "NextVer", key: CryptoData.ValidRsaKey)
        def v = new Version(id: 2L, name: "1.1.0")
        project.addVersion(v)

        when:
        project.removeVersion(v)

        then:
        project.versions.isEmpty()
    }

    def "clearVersions should empty list and detach all"() {
        given:
        def project = new Project(name: "NextVer", key: CryptoData.ValidRsaKey)
        def v1 = new Version(id: 3L, name: "1.0.0")
        def v2 = new Version(id: 4L, name: "1.1.0")
        project.addVersion(v1).addVersion(v2)

        when:
        project.clearVersions()

        then:
        project.versions.isEmpty()
    }

    def "removeVersion on non-contained version should be no-op"() {
        given:
        def project = new Project(name: "NextVer", key: CryptoData.ValidRsaKey)
        def v = new Version(id: 5L, name: "1.2.0")

        when:
        project.removeVersion(v)

        then:
        project.versions.isEmpty()
    }

    def "should validate key format"() {
        given:
        def project = new Project()

        when:
        project.key = "not-a-valid-ssh-key"

        then:
        thrown(IllegalArgumentException)
    }

    def "should validate non-empty name via setter"() {
        given:
        def project = new Project(name: "Ok", key: CryptoData.ValidRsaKey)

        when:
        project.name = ""

        then:
        thrown(IllegalArgumentException)
    }

    def "should reject construction with empty name"() {
        when:
        new Project(name: "", key: CryptoData.ValidRsaKey)

        then:
        thrown(IllegalArgumentException)
    }

    def "should reject construction with null name"() {
        when:
        new Project(name: null, key: CryptoData.ValidRsaKey)

        then:
        thrown(IllegalArgumentException)
    }
}
