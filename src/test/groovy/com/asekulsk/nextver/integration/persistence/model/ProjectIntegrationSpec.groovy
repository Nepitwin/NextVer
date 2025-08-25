package com.asekulsk.nextver.integration.persistence.model

import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.model.Version
import com.asekulsk.nextver.util.CryptoData
import com.asekulsk.nextver.util.PersistenceGenerator
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class ProjectIntegrationSpec extends Specification {

    @Autowired
    EntityManager entityManager

    def "should persist project with name and key"() {
        given:
        def project = PersistenceGenerator.GenerateProject()

        when:
        entityManager.persist(project)
        entityManager.flush()

        then:
        project.id != null
    }

    def "should fail when persisting without name"() {
        given:
        def project = new Project(key: CryptoData.ValidRsaKey)

        when:
        entityManager.persist(project)
        entityManager.flush()

        then:
        thrown(Exception)
    }

    def "should fail when persisting without key"() {
        given:
        def project = new Project(name: "MyProject")

        when:
        entityManager.persist(project)
        entityManager.flush()

        then:
        thrown(Exception)
    }

    def "should cascade persist versions"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        def version = PersistenceGenerator.GenerateVersion()
        project.addVersion(version)

        when:
        entityManager.persist(project)
        entityManager.flush()
        entityManager.clear()

        then: "project and version should both be persisted"
        def persistedProject = entityManager.find(Project, project.id)
        persistedProject.versions.size() == 1
        persistedProject.versions[0].name == version.name
    }

    def "should remove orphan versions"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        def version = PersistenceGenerator.GenerateVersion()

        project.addVersion(version)
        entityManager.persist(project)
        entityManager.flush()

        when: "remove the version from project"
        project.versions.clear()
        entityManager.flush()
        entityManager.clear()

        then: "version should be deleted from DB"
        def persistedProject = entityManager.find(Project, project.id)
        persistedProject.versions.isEmpty()

        def entity = entityManager.find(Version, version.id)
        entity.is(null)
    }
}
