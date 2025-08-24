package com.asekulsk.nextver.integration.persistence.model

import com.asekulsk.nextver.persistence.model.Version
import com.asekulsk.nextver.util.PersistenceGenerator
import com.asekulsk.nextver.util.VersioningGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

import jakarta.persistence.EntityManager

@DataJpaTest
class VersionIntegrationSpec extends Specification {

    @Autowired
    EntityManager entityManager

    def "should support persisting versions with all VersionType enum values"() {
        given:
        Version version = versionFactory()

        when:
        entityManager.persist(version)
        entityManager.flush()

        then:
        version.id != null

        where:
        versionFactory << VersioningGenerator.versionFactories
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
