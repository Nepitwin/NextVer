package com.asekulsk.nextver.api.controller

import com.asekulsk.nextver.api.exceptions.InvalidDataException
import com.asekulsk.nextver.persistence.interfaces.IProjectService
import com.asekulsk.nextver.persistence.model.Project
import spock.lang.Specification

class ProjectControllerSpec extends Specification {

    IProjectService projectService = Mock()
    ProjectController projectController = new ProjectController(projectService)

    def "should register a project successfully"() {
        given:
        def projectName = "TestProject"
        projectService.register(projectName) >> true

        when:
        def result = projectController.register(projectName)

        then:
        result
    }

    def "should throw InvalidDataException for blank or empty project name"() {
        when:
        projectController.register(name)

        then:
        thrown(InvalidDataException)

        where:
        name << ["", "   ", null]
    }

    def "should call projectService.register with correct name"() {
        given:
        def projectName = "MyProject"

        when:
        projectController.register(projectName)

        then:
        1 * projectService.register(projectName)
    }

    def "should return project when getProjectByName is called"() {
        given:
        def projectName = "ExistingProject"
        def project = new Project(name: projectName)
        projectService.getProjectByName(projectName) >> project

        when:
        def result = projectController.getProjectByName(projectName)

        then:
        result == project
    }

    def "should pass correct name to projectService.getProjectByName"() {
        given:
        def projectName = "AnotherProject"
        def project = new Project(name: projectName)
        projectService.getProjectByName(projectName) >> project

        when:
        projectController.getProjectByName(projectName)

        then:
        1 * projectService.getProjectByName(projectName)
    }
}
