package com.asekulsk.nextver.persistence.model

import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class ProjectPersistenceSpec extends Specification {

    @Autowired
    EntityManager entityManager

    def "should persist project with name"() {
        given:
        def project = new Project(name: "NextVer")

        when:
        entityManager.persist(project)
        entityManager.flush()

        then:
        project.id != null
    }

    def "should fail when persisting without name"() {
        given:
        def project = new Project()

        when:
        entityManager.persist(project)
        entityManager.flush()

        then:
        thrown(Exception)
    }
}
