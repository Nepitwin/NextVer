package com.asekulsk.nextver.persistence.service

import com.asekulsk.nextver.api.exceptions.DataNotFoundException
import com.asekulsk.nextver.api.exceptions.InvalidDataException
import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.persistence.model.Version
import com.asekulsk.nextver.persistence.repository.ProjectRepository
import com.asekulsk.nextver.persistence.repository.VersionRepository
import spock.lang.Specification

class VersionServiceSpec extends Specification {

    VersionRepository versionRepository = Mock()
    ProjectRepository projectRepository = Mock()
    VersionService versionService = new VersionService(versionRepository, projectRepository)

    def "should register a valid version successfully"() {
        given:
        def project = [name: "TestProject"]
        def versionName = "v1"
        def versionValue = "1.0.0"

        projectRepository.findByName("TestProject") >> Optional.of(project)

        when:
        def result = versionService.register("TestProject", versionName, versionValue, VersionType.SEMVER)

        then:
        1 * versionRepository.save(_) >> new Version()
        result
    }

    def "should throw DataNotFoundException if project not found"() {
        given:
        projectRepository.findByName("UnknownProject") >> Optional.empty()

        when:
        versionService.register("UnknownProject", "v1", "1.0.0", VersionType.SEMVER)

        then:
        thrown(DataNotFoundException)
    }

    def "should throw InvalidDataException if version format is invalid"() {
        given:
        def project = [name: "TestProject"]
        projectRepository.findByName("TestProject") >> Optional.of(project)

        when:
        versionService.register("TestProject", "v1", "invalid-version", VersionType.SEMVER)

        then:
        thrown(InvalidDataException)
    }

    def "should throw DataNotFoundException if version name already exists"() {
        given:
        def existingVersion = [name: "v1", version: "1.0.0"]
        def project = [name: "TestProject", versions: [existingVersion]]
        projectRepository.findByName("TestProject") >> Optional.of(project)

        when:
        versionService.register("TestProject", "v1", "1.0.1", VersionType.SEMVER)

        then:
        thrown(DataNotFoundException)
    }

    def "should get next version successfully"() {
        given:
        def version = new Version(name: "v1", version: "1.0.0", type: VersionType.SEMVER)
        def project = [name: "TestProject", versions: [version]]
        projectRepository.findByName("TestProject") >> Optional.of(project)

        when:
        versionService.getNextVersion("TestProject", "v1", VersionIncrementType.MINOR)

        then:
        1 * versionRepository.save(_) >> { Version v ->
            assert v.version != "1.0.0"  // version should be incremented
            v
        }
    }

    def "should throw DataNotFoundException if version not found when getting next version"() {
        given:
        def project = [name: "TestProject"]
        projectRepository.findByName("TestProject") >> Optional.of(project)

        when:
        versionService.getNextVersion("TestProject", "v1", VersionIncrementType.MINOR)

        then:
        thrown(DataNotFoundException)
    }

    def "should throw DataNotFoundException if project not found when getting next version"() {
        given:
        projectRepository.findByName("UnknownProject") >> Optional.empty()

        when:
        versionService.getNextVersion("UnknownProject", "v1", VersionIncrementType.MINOR)

        then:
        thrown(DataNotFoundException)
    }
}
