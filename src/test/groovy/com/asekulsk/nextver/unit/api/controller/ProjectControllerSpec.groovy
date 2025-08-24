package com.asekulsk.nextver.unit.api.controller

import com.asekulsk.nextver.api.controller.ProjectController
import com.asekulsk.nextver.api.exceptions.DataNotFoundException
import com.asekulsk.nextver.api.exceptions.InvalidDataException
import com.asekulsk.nextver.api.request.NextVersionRequest
import com.asekulsk.nextver.api.request.RegisterRequest
import com.asekulsk.nextver.api.request.RegisterVersionRequest
import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.domain.enumeration.VersionType
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.persistence.interfaces.IProjectService
import com.asekulsk.nextver.persistence.model.Version
import com.asekulsk.nextver.util.CryptoData
import com.asekulsk.nextver.util.PersistenceGenerator
import spock.lang.Specification

class ProjectControllerSpec extends Specification {

    IProjectService projectService = Mock()
    ProjectController projectController = new ProjectController(projectService)

    def "should throw InvalidDataException for blank or empty project name"() {
        given:
        def request = new RegisterRequest(name: name, key: CryptoData.ValidRsaKey)

        when:
        projectController.register(request)

        then:
        thrown(InvalidDataException)

        where:
        name << ["", "   ", null]
        key << ["", "   ", null]
    }

    def "should call register with correct name"() {
        given:
        def request = new RegisterRequest(name: "MyProject", key: CryptoData.ValidRsaKey)

        when:
        projectController.register(request)

        then:
        1 * projectService.register(request.name, request.key)
    }

    def "should convert NextVerException into API exception from register call"() {
        given:
        def request = new RegisterRequest(name: "MyProject", key: CryptoData.ValidRsaKey)

        def ex = new NextVerException("Not found", NextVerReason.DataNotFound)
        projectService.register(request.name, request.key) >> { throw ex }

        when:
        projectController.register(request)

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

    def "should call register and return true"() {
        given:
        def project = "TestProject"
        def request = new RegisterVersionRequest(versionName: "v1", version: "1.0.0", type: VersionType.SEMVER)
        projectService.register(project, request.versionName, request.version, request.type) >> true

        when:
        def result = projectController.register(project, request)

        then:
        result
    }

    def "should pass correct parameters to register"() {
        given:
        def project = "TestProject"
        def request = new RegisterVersionRequest(versionName: "v2", version: "2.0.0", type: VersionType.FOUR_PART)

        when:
        projectController.register(project, request)

        then:
        1 * projectService.register(project, "v2", "2.0.0", VersionType.FOUR_PART)
    }

    def "should call next and return next version string"() {
        given:
        def project = "TestProject"
        def request = new NextVersionRequest(versionName: "v1", versionIncrementType: VersionIncrementType.MINOR)
        def nextVersion = new Version(version: "1.1.0")

        projectService.getNextVersion(project, request.versionName, request.versionIncrementType) >> nextVersion

        when:
        def result = projectController.next(project, request)

        then:
        result == "1.1.0"
    }

    def "should pass correct parameters to next"() {
        given:
        def project = "TestProject"
        def request = new NextVersionRequest(versionName: "v2", versionIncrementType: VersionIncrementType.MAJOR)
        def version = new Version(version: "3.0.0")
        projectService.getNextVersion(project, "v2", VersionIncrementType.MAJOR) >> version

        when:
        def result = projectController.next(project, request)

        then:
        result == "3.0.0"
    }
}
