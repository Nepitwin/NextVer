package com.asekulsk.nextver.integration.persistence.service

import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.persistence.model.Version
import com.asekulsk.nextver.persistence.repository.ProjectRepository
import com.asekulsk.nextver.persistence.repository.VersionRepository
import com.asekulsk.nextver.persistence.service.VersionService
import com.asekulsk.nextver.util.PersistenceGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
class VersionServiceIntegrationSpec extends Specification {

    @Autowired
    VersionService versionService

    @Autowired
    ProjectRepository projectRepository

    @Autowired
    VersionRepository versionRepository

    def cleanup() {
        versionRepository.deleteAll()
        projectRepository.deleteAll()
    }

    @Unroll
    @Transactional
    def "register saves new version"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectRepository.saveAndFlush(project)

        def versionName = "main"
        def versionValue = (type == VersionType.SEMVER) ? "1.2.3" : "1.2.3.4"

        when:
        def ok = versionService.register(project.name, versionName, versionValue, type)

        then:
        ok
        def stored = versionRepository.findAll().first()
        stored.name == versionName
        stored.version == versionValue
        stored.type == type

        where:
        type << [VersionType.SEMVER, VersionType.FOUR_PART]
    }

    def "register throws DataAlreadyExists if the version name is the same"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectRepository.saveAndFlush(project)

        versionService.register(project.name, "api", "0.1.0", VersionType.SEMVER)

        when:
        versionService.register(project.name, "api", "0.2.0", VersionType.SEMVER)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataAlreadyExists
    }

    def "register throws DataNotFound for unknown project"() {
        when:
        versionService.register("unknown-proj", "core", "1.0.0", VersionType.SEMVER)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound
    }

    def "register throws InvalidData if the version format is invalid"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectRepository.saveAndFlush(project)

        when:
        versionService.register(project.name, "bad", "abc.def", VersionType.SEMVER)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.InvalidData
    }

    @Unroll
    def "getNextVersion increments correctly"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectRepository.saveAndFlush(project)

        def initial = (type == VersionType.SEMVER) ? "1.2.3" : "1.2.3.4"
        versionService.register(project.name, "stream", initial, type)

        when:
        Version updated = versionService.getNextVersion(project.name, "stream", increment)

        then:
        updated.version != initial

        where:
        type                         | increment
        VersionType.SEMVER           | VersionIncrementType.PATCH
        VersionType.SEMVER           | VersionIncrementType.MINOR
        VersionType.SEMVER           | VersionIncrementType.MAJOR
        VersionType.FOUR_PART        | VersionIncrementType.PATCH
        VersionType.FOUR_PART        | VersionIncrementType.MINOR
        VersionType.FOUR_PART        | VersionIncrementType.MAJOR
    }

    def "getNextVersion throws DataNotFound if the project is unknown"() {
        when:
        versionService.getNextVersion("missing", "x", VersionIncrementType.MINOR)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound
    }

    def "getNextVersion throws DataNotFound if the version is missing from the project"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectRepository.saveAndFlush(project)

        when:
        versionService.getNextVersion(project.name, "nope", VersionIncrementType.MINOR)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound
    }
}
