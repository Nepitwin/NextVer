package com.asekulsk.nextver.unit.persistence.service

import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.repository.ProjectRepository
import com.asekulsk.nextver.persistence.service.ProjectService
import com.asekulsk.nextver.util.PersistenceGenerator
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
}
