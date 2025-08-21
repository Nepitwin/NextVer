package com.asekulsk.nextver.persistence.model

import com.asekulsk.nextver.domain.enumeration.VersionType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

import jakarta.persistence.EntityManager

@DataJpaTest
class VersionPersistenceSpec extends Specification {

    @Autowired
    EntityManager entityManager

    def "should persist version with all required fields"() {
        given:
        def project = new Project(name: "NextVer")
        entityManager.persist(project)

        def version = new Version(
                name: "Initial Release",
                type: VersionType.SEMVER,
                version: "1.0.0",
                project: project
        )

        when:
        entityManager.persist(version)
        entityManager.flush()

        then:
        version.id != null
        version.project.id == project.id
        version.type == VersionType.SEMVER
    }

    def "should support persisting versions with all VersionType enum values"() {
        given:
        def project = new Project(name: "NextVer")
        entityManager.persist(project)

        when:
        def version = new Version(
                name: "Version Test",
                type: type,
                version: "1.0.0",
                project: project
        )
        entityManager.persist(version)
        entityManager.flush()

        then:
        version.id != null
        version.type == type

        where:
        type << VersionType.values()
    }

    def "should fail when persisting without required fields"() {
        given:
        def version = new Version()

        when:
        entityManager.persist(version)
        entityManager.flush()

        then:
        thrown(Exception)
    }
}
