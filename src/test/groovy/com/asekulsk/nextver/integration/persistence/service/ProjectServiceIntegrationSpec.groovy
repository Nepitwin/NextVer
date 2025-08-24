package com.asekulsk.nextver.integration.persistence.service

import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.repository.ProjectRepository
import com.asekulsk.nextver.persistence.service.ProjectService
import com.asekulsk.nextver.util.CryptoData
import com.asekulsk.nextver.util.PersistenceGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
class ProjectServiceIntegrationSpec extends Specification {

    @Autowired
    ProjectService projectService

    @Autowired
    ProjectRepository projectRepository

    def cleanup() {
        projectRepository.deleteAll()
    }

    def "register should persist a new project"() {
        given:
        String name = "MyProject"
        String key = CryptoData.ValidRsaKey

        when:
        def result = projectService.register(name, key)

        then:
        result
    }

    def "duplicate register throws NextVerException if first project exists"() {
        given:
        def p = new Project(name: "MyProject", key: CryptoData.ValidRsaKey)
        projectRepository.saveAndFlush(p)

        when:
        projectService.register(p.name, p.key)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataAlreadyExists
    }

    def "getProjectByName returns existing project"() {
        given:
        def p = new Project(name: "MyProject", key: CryptoData.ValidRsaKey)
        projectRepository.saveAndFlush(p)

        when:
        def loaded = projectService.getProjectByName(p.name)

        then:
        loaded.id != null
        loaded.name == p.name
    }

    def "getProjectByName throws NextVerException if the name is unknown"() {
        when:
        projectService.getProjectByName("MyProject")

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound
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
        def ok = projectService.register(project.name, versionName, versionValue, type)

        then:
        ok
        def stored = projectRepository.findAll().first()
        stored.versions.size() == 1
        stored.versions[0].name == versionName
        stored.versions[0].version == versionValue
        stored.versions[0].type == type

        where:
        type << [VersionType.SEMVER, VersionType.FOUR_PART]
    }

    @Transactional
    def "register throws DataAlreadyExists if the version name is the same"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectRepository.saveAndFlush(project)

        projectService.register(project.name, "api", "0.1.0", VersionType.SEMVER)

        when:
        projectService.register(project.name, "api", "0.2.0", VersionType.SEMVER)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataAlreadyExists
    }

    def "register throws DataNotFound for unknown project"() {
        when:
        projectService.register("unknown-proj", "core", "1.0.0", VersionType.SEMVER)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound
    }

    def "register throws InvalidData if the version format is invalid"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectRepository.saveAndFlush(project)

        when:
        projectService.register(project.name, "bad", "abc.def", VersionType.SEMVER)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.InvalidData
    }

    @Unroll
    @Transactional
    def "getNextVersion increments correctly"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectRepository.saveAndFlush(project)

        def initial = (type == VersionType.SEMVER) ? "1.2.3" : "1.2.3.4"
        projectService.register(project.name, "stream", initial, type)

        when:
        def updated = projectService.getNextVersion(project.name, "stream", increment)

        then:
        updated
        project.versions.size() == 1
        project.versions[0].version != initial

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
        projectService.getNextVersion("missing", "x", VersionIncrementType.MINOR)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound
    }

    @Transactional
    def "getNextVersion throws DataNotFound if the version is missing from the project"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectRepository.saveAndFlush(project)

        when:
        projectService.getNextVersion(project.name, "nope", VersionIncrementType.MINOR)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound
    }
}
