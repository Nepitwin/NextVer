package com.asekulsk.nextver.api.controller

import com.asekulsk.nextver.api.exceptions.InvalidDataException
import com.asekulsk.nextver.api.request.NextVersionRequest
import com.asekulsk.nextver.api.request.RegisterRequest
import com.asekulsk.nextver.api.request.RegisterVersionRequest
import com.asekulsk.nextver.api.util.NextVerExceptionConverter
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.persistence.interfaces.IProjectService
import com.asekulsk.nextver.persistence.model.Project
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/project")
class ProjectController {

    private final IProjectService projectService

    ProjectController(IProjectService projectService) {
        this.projectService = projectService
    }

    @GetMapping("/get/{name}")
    Project getProjectByName(@PathVariable("name") String name) {
        try
        {
            projectService.getProjectByName(name)
        }
        catch (NextVerException ex)
        {
            throw NextVerExceptionConverter.CastToException(ex)
        }
    }

    @PostMapping("/register")
    boolean register(@RequestBody RegisterRequest body) {

        def name = body.name
        def key = body.key

        if (name == null || name.isBlank() || name.isEmpty() || name.isAllWhitespace())
        {
            throw new InvalidDataException("Empty 'name' data field is not allowed")
        }

        if (key == null || key.isBlank() || key.isEmpty() || key.isAllWhitespace())
        {
            throw new InvalidDataException("Empty 'key' data field is not allowed")
        }

        try
        {
            projectService.register(name, key)
        }
        catch (NextVerException ex)
        {
            throw NextVerExceptionConverter.CastToException(ex)
        }
    }

    @PostMapping("/register/version/{project}")
    boolean register(@PathVariable("project") String project, @RequestBody RegisterVersionRequest body) {
        try
        {
            projectService.register(project, body.versionName, body.version, body.type)
        }
        catch (NextVerException ex)
        {
            throw NextVerExceptionConverter.CastToException(ex)
        }
    }

    @GetMapping("/next/version/{project}")
    String next(@PathVariable("project") String project, @RequestBody NextVersionRequest body) {
        try
        {
            projectService.getNextVersion(project, body.versionName, body.versionIncrementType).version
        }
        catch (NextVerException ex)
        {
            throw NextVerExceptionConverter.CastToException(ex)
        }
    }
}
