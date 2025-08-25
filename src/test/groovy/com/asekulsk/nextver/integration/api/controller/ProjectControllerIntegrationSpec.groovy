package com.asekulsk.nextver.integration.api.controller

import com.asekulsk.nextver.api.request.NextVersionRequest
import com.asekulsk.nextver.api.request.RegisterRequest
import com.asekulsk.nextver.api.request.RegisterVersionRequest
import com.asekulsk.nextver.domain.enumeration.VersionIncrementType
import com.asekulsk.nextver.persistence.interfaces.IProjectService
import com.asekulsk.nextver.persistence.model.Version
import com.asekulsk.nextver.util.PersistenceGenerator
import com.asekulsk.nextver.util.VersioningGenerator
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ProjectControllerIntegrationSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    IProjectService projectService

    def "should register a new project successfully"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        def body = new RegisterRequest(name: project.name, key: project.key)

        expect:
        mockMvc.perform(post("/api/project/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"${body.name}","key":"${body.key}"}"""))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
    }

    def "should fail registering with empty name"() {
        expect:
        mockMvc.perform(post("/api/project/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"   ","key":"abc"}"""))
                .andExpect(status().isBadRequest())
    }

    def "should fetch project by name"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectService.register(project.name, project.key)

        expect:
        mockMvc.perform(get("/api/project/get/${project.name}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').value(project.name))
                .andExpect(jsonPath('$.key').value(project.key))
    }

    def "should register version for project"() {
        given:
        def project = PersistenceGenerator.GenerateProject()
        projectService.register(project.name, project.key)

        Version version = versionFactory()

        def versionReq = new RegisterVersionRequest(versionName: version.name, version: version.version, type: version.type)

        expect:
        mockMvc.perform(post("/api/project/register/version/${project.name}")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"versionName":"${versionReq.versionName}","version":"${versionReq.version}","type":"${versionReq.type}"}"""))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))

        where:
        versionFactory << VersioningGenerator.versionFactories
        versionIncrementType << [VersionIncrementType.PATCH, VersionIncrementType.MAJOR]
    }

    def "should return next version for project"() {
        given:
        Version version = versionFactory()

        def project = PersistenceGenerator.GenerateProject()
        projectService.register(project.name, project.key)

        projectService.register(project.name, version.name, version.version, version.type)

        def req = new NextVersionRequest(versionName: version.name, versionIncrementType: versionIncrementType)

        expect:
        mockMvc.perform(get("/api/project/next/version/${project.name}")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"versionName":"${req.versionName}","versionIncrementType":"${req.versionIncrementType}"}"""))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString(".")))

        where:
        versionFactory << VersioningGenerator.versionFactories
        versionIncrementType << [VersionIncrementType.PATCH, VersionIncrementType.MAJOR]
    }
}
