package com.asekulsk.nextver.persistence.model

import spock.lang.Specification

class ProjectSpec extends Specification {

    def "should allow setting and getting fields"() {
        given:
        def project = new Project()

        when:
        project.id = 1L
        project.name = "NextVer"

        then:
        project.id == 1L
        project.name == "NextVer"
    }

    def "should initialize versions as empty list"() {
        when:
        def project = new Project()

        then:
        project.versions != null
        project.versions.isEmpty()
    }

    def "should allow adding and removing versions"() {
        given:
        def project = new Project(name: "NextVer")
        def version = new Version(id: 1L, name: "1.0.0", project: project)

        when:
        project.versions << version

        then:
        project.versions.size() == 1
        project.versions[0].project == project

        when: "removing the version"
        project.versions.remove(version)

        then:
        project.versions.isEmpty()
    }
}
