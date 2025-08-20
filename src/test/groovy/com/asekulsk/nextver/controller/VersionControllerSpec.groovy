package com.asekulsk.nextver.controller

import com.asekulsk.nextver.enumeration.VersionIncrementType
import com.asekulsk.nextver.enumeration.VersionType
import com.asekulsk.nextver.interfaces.IVersionService
import com.asekulsk.nextver.persistence.Version
import com.asekulsk.nextver.request.NextVersionRequest
import com.asekulsk.nextver.request.RegisterVersionRequest
import spock.lang.Specification

class VersionControllerSpec extends Specification {

    IVersionService versionService = Mock()
    VersionController versionController = new VersionController(versionService)

    def "should call register and return true"() {
        given:
        def project = "TestProject"
        def request = new RegisterVersionRequest(versionName: "v1", version: "1.0.0", type: VersionType.SEMVER)
        versionService.register(project, request.versionName, request.version, request.type) >> true

        when:
        def result = versionController.register(project, request)

        then:
        result
    }

    def "should call next and return next version string"() {
        given:
        def project = "TestProject"
        def request = new NextVersionRequest(versionName: "v1", versionIncrementType: VersionIncrementType.MINOR)
        def nextVersion = new Version(version: "1.1.0")

        versionService.getNextVersion(project, request.versionName, request.versionIncrementType) >> nextVersion

        when:
        def result = versionController.next(project, request)

        then:
        result == "1.1.0"
    }

    def "should pass correct parameters to register"() {
        given:
        def project = "TestProject"
        def request = new RegisterVersionRequest(versionName: "v2", version: "2.0.0", type: VersionType.FOUR_PART)

        when:
        versionController.register(project, request)

        then:
        1 * versionService.register(project, "v2", "2.0.0", VersionType.FOUR_PART)
    }

    def "should pass correct parameters to next"() {
        given:
        def project = "TestProject"
        def request = new NextVersionRequest(versionName: "v2", versionIncrementType: VersionIncrementType.MAJOR)
        def version = new Version(version: "3.0.0")
        versionService.getNextVersion(project, "v2", VersionIncrementType.MAJOR) >> version

        when:
        def result = versionController.next(project, request)

        then:
        result == "3.0.0"
    }
}
