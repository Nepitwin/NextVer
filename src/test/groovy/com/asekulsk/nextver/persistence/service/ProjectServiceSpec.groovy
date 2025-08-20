package com.asekulsk.nextver.persistence.service

import com.asekulsk.nextver.api.exceptions.DataNotFoundException
import com.asekulsk.nextver.api.exceptions.ProjectAlreadyExistsException
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.repository.ProjectRepository
import spock.lang.Specification

class ProjectServiceSpec extends Specification {

    ProjectRepository projectRepository = Mock()
    ProjectService projectService = new ProjectService(projectRepository)

    def "should register a new project successfully"() {
        given:
        def projectName = "NewProject"
        projectRepository.findByName(projectName) >> Optional.empty()

        when:
        def result = projectService.register(projectName)

        then:
        1 * projectRepository.save(_) >> { Project p ->
            assert p.name == projectName
            p
        }
        result
    }

    def "should throw ProjectAlreadyExistsException if project already exists"() {
        given:
        def projectName = "ExistingProject"
        projectRepository.findByName(projectName) >> Optional.of(new Project(name: projectName))

        when:
        projectService.register(projectName)

        then:
        thrown(ProjectAlreadyExistsException)
    }

    def "should return project when found by name"() {
        given:
        def projectName = "ExistingProject"
        def project = new Project(name: projectName)
        projectRepository.findByName(projectName) >> Optional.of(project)

        when:
        def result = projectService.getProjectByName(projectName)

        then:
        result == project
    }

    def "should throw DataNotFoundException if project not found by name"() {
        given:
        def projectName = "UnknownProject"
        projectRepository.findByName(projectName) >> Optional.empty()

        when:
        projectService.getProjectByName(projectName)

        then:
        thrown(DataNotFoundException)
    }
}
