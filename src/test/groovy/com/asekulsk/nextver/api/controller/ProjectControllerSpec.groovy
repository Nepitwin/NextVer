package com.asekulsk.nextver.api.controller

import com.asekulsk.nextver.api.exceptions.DataNotFoundException
import com.asekulsk.nextver.api.exceptions.InvalidDataException
import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.persistence.interfaces.IProjectService
import com.asekulsk.nextver.util.PersistenceGenerator
import spock.lang.Specification

class ProjectControllerSpec extends Specification {

    IProjectService projectService = Mock()
    ProjectController projectController = new ProjectController(projectService)

    def "should register a project"() {
        given:
        def projectName = "TestProject"
        projectService.register(projectName) >> expectedResult

        when:
        def result = projectController.register(projectName)

        then:
        result == expectedResult

        where:
        expectedResult << [true, false]
    }

    def "should throw InvalidDataException for blank or empty project name"() {
        when:
        projectController.register(name)

        then:
        thrown(InvalidDataException)

        where:
        name << ["", "   ", null]
    }

    def "should call register with correct name"() {
        given:
        def projectName = "MyProject"

        when:
        projectController.register(projectName)

        then:
        1 * projectService.register(projectName)
    }

    def "should convert NextVerException into API exception from register call"() {
        given:
        def ex = new NextVerException("Not found", NextVerReason.DataNotFound)
        projectService.register("Unknown") >> { throw ex }

        when:
        projectController.register("Unknown")

        then:
        def thrownEx = thrown(DataNotFoundException)
        thrownEx.message == "Not found"
    }

    def "should return project when getProjectByName is called"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectService.getProjectByName(project.name) >> project

        when:
        def result = projectController.getProjectByName(project.name)

        then:
        result == project
    }

    def "should pass correct name to getProjectByName"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectService.getProjectByName(project.name) >> project

        when:
        projectController.getProjectByName(project.name)

        then:
        1 * projectService.getProjectByName(project.name)
    }

    def "should convert NextVerException into API exception from getProjectByName"() {
        given:
        def ex = new NextVerException("Not found", NextVerReason.DataNotFound)
        projectService.getProjectByName("Unknown") >> { throw ex }

        when:
        projectController.getProjectByName("Unknown")

        then:
        def thrownEx = thrown(DataNotFoundException)
        thrownEx.message == "Not found"
    }
}
