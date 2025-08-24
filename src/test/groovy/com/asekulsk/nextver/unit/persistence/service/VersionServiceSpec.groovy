package com.asekulsk.nextver.unit.persistence.service

import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.model.Version
import com.asekulsk.nextver.persistence.repository.ProjectRepository
import com.asekulsk.nextver.persistence.repository.VersionRepository
import com.asekulsk.nextver.persistence.service.VersionService
import com.asekulsk.nextver.util.PersistenceGenerator
import com.asekulsk.nextver.util.VersioningGenerator
import spock.lang.Specification

class VersionServiceSpec extends Specification {

    VersionRepository versionRepository = Mock()
    ProjectRepository projectRepository = Mock()
    VersionService versionService = new VersionService(versionRepository, projectRepository)

    def "should register a valid version successfully"() {
        given:
        Project project = PersistenceGenerator.GenerateProject()
        Version version = versionFactory()

        projectRepository.findByName(project.name) >> Optional.of(project)

        when:
        def result = versionService.register(project.name, version.name, version.version, version.type)

        then:
        1 * projectRepository.save(_) >> new Project()
        result

        where:
        versionFactory << VersioningGenerator.versionFactories
    }

    def "should throw NextVerReason if project not found"() {
        given:
        Project project = PersistenceGenerator.GenerateProject()
        Version version = versionFactory()

        projectRepository.findByName(project.name) >> Optional.empty()

        when:
        versionService.register(project.name, version.name, version.version, version.type)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound

        where:
        versionFactory << VersioningGenerator.versionFactories
    }

    def "should throw NextVerReason if version format is invalid"() {
        given:
        Project project = PersistenceGenerator.GenerateProject()
        Version version = versionFactory()

        projectRepository.findByName(project.name) >> Optional.of(project)

        when:
        versionService.register(project.name, version.name, "invalid-version", version.type)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.InvalidData

        where:
        versionFactory << VersioningGenerator.versionFactories
    }

    def "should throw NextVerReason if version name already exists"() {
        given:
        Project project = PersistenceGenerator.GenerateProject()
        Version version = versionFactory()

        project.versions = [version]
        projectRepository.findByName(project.name) >> Optional.of(project)

        when:
        versionService.register(project.name, version.name, version.version, version.type)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataAlreadyExists

        where:
        versionFactory << VersioningGenerator.versionFactories
    }

    def "should get next version successfully"() {
        given:
        Project project = PersistenceGenerator.GenerateProject()
        Version version = versionFactory()

        project.versions = [version]
        projectRepository.findByName(project.name) >> Optional.of(project)
        def old_version = version.version

        when:
        versionService.getNextVersion(project.name, version.name, VersionIncrementType.MINOR)

        then:
        1 * versionRepository.save(_) >> { Version v ->
            assert v.version != old_version
            v
        }

        where:
        versionFactory << VersioningGenerator.versionFactories
    }

    def "should throw NextVerReason if version not found when getting next version"() {
        given:
        Project project = PersistenceGenerator.GenerateProject()
        Version version = versionFactory()

        projectRepository.findByName(project.name) >> Optional.of(project)

        when:
        versionService.getNextVersion(project.name, version.name, VersionIncrementType.MINOR)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound

        where:
        versionFactory << VersioningGenerator.versionFactories
    }

    def "should throw NextVerReason if project not found when getting next version"() {
        given:
        Project project = PersistenceGenerator.GenerateProject()
        Version version = versionFactory()

        projectRepository.findByName(project.name) >> Optional.empty()

        when:
        versionService.getNextVersion(project.name, version.name, VersionIncrementType.MINOR)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound

        where:
        versionFactory << VersioningGenerator.versionFactories
    }
}
