package com.asekulsk.nextver.integration.persistence.service

import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.persistence.model.Project
import com.asekulsk.nextver.persistence.repository.ProjectRepository
import com.asekulsk.nextver.persistence.service.ProjectService
import com.asekulsk.nextver.util.CryptoData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ProjectServiceIntegrationSpec extends Specification {

    @Autowired
    ProjectService projectService

    @Autowired
    ProjectRepository projectRepository

    def cleanup() {
        projectRepository.deleteAll()
    }

    def "register should persist a new project"() {
        given:
        String name = "MyProject"
        String key = CryptoData.ValidRsaKey

        when:
        def result = projectService.register(name, key)

        then:
        result
    }

    def "duplicate register throws NextVerException if first project exists"() {
        given:
        def p = new Project(name: "MyProject", key: CryptoData.ValidRsaKey)
        projectRepository.saveAndFlush(p)

        when:
        projectService.register(p.name, p.key)

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataAlreadyExists
    }

    def "getProjectByName returns existing project"() {
        given:
        def p = new Project(name: "MyProject", key: CryptoData.ValidRsaKey)
        projectRepository.saveAndFlush(p)

        when:
        def loaded = projectService.getProjectByName(p.name)

        then:
        loaded.id != null
        loaded.name == p.name
    }

    def "getProjectByName throws NextVerException if the name is unknown"() {
        when:
        projectService.getProjectByName("MyProject")

        then:
        def ex = thrown(NextVerException)
        ex.reason == NextVerReason.DataNotFound
    }
}
