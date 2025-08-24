package com.asekulsk.nextver.unit.persistence.service

import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.model.Version
import com.asekulsk.nextver.persistence.repository.ProjectRepository
import com.asekulsk.nextver.persistence.service.ProjectService
import com.asekulsk.nextver.util.PersistenceGenerator
import com.asekulsk.nextver.util.VersioningGenerator
import spock.lang.Specification

class ProjectServiceSpec extends Specification {

    ProjectRepository projectRepository = Mock()
    ProjectService projectService = new ProjectService(projectRepository)

    def "should register a new project successfully"() {
        given:
        Project project = PersistenceGenerator.GenerateProject()

        projectRepository.findByName(project.name) >> Optional.empty()

        when:
        def result = projectService.register(project.name, project.key)

        then:
        1 * projectRepository.save(_) >> { Project p ->
            assert p.name == project.name
            p
        }
        result
    }

    def "should throw NextVerException if project already exists"() {
        given:
        Project project = PersistenceGenerator.GenerateProject()

        projectRepository.findByName(project.name) >> Optional.of(project)

        when:
        projectService.register(project.name, project.key)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataAlreadyExists
    }

    def "should return project when found by name"() {
        given:
        Project project = PersistenceGenerator.GenerateProject()

        projectRepository.findByName(project.name) >> Optional.of(project)

        when:
        def result = projectService.getProjectByName(project.name)

        then:
        result == project
    }

    def "should throw NextVerReason if project not found by name"() {
        given:
        Project project = PersistenceGenerator.GenerateProject()

        projectRepository.findByName(project.name) >> Optional.empty()

        when:
        projectService.getProjectByName(project.name)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound
    }

    def "should register a valid version successfully"() {
        given:
        Project project = PersistenceGenerator.GenerateProject()
        Version version = versionFactory()

        projectRepository.findByName(project.name) >> Optional.of(project)

        when:
        def result = projectService.register(project.name, version.name, version.version, version.type)

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
        projectService.register(project.name, version.name, version.version, version.type)

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
        projectService.register(project.name, version.name, "invalid-version", version.type)

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
        projectService.register(project.name, version.name, version.version, version.type)

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
        projectService.getNextVersion(project.name, version.name, VersionIncrementType.MINOR)

        then:
        1 * projectRepository.save(_) >> { Project p ->
            assert p.versions[0].version != old_version
            p
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
        projectService.getNextVersion(project.name, version.name, VersionIncrementType.MINOR)

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
        projectService.getNextVersion(project.name, version.name, VersionIncrementType.MINOR)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound

        where:
        versionFactory << VersioningGenerator.versionFactories
    }
}
