package com.asekulsk.nextver.api.controller

import com.asekulsk.nextver.api.exceptions.InvalidDataException
import com.asekulsk.nextver.api.util.NextVerExceptionConverter
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.persistence.interfaces.IProjectService
import com.asekulsk.nextver.persistence.model.Project
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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

    @PostMapping("/register/{name}")
    boolean register(@PathVariable("name") String name) {

        if (name == null || name.isBlank() || name.isEmpty() || name.isAllWhitespace())
        {
            throw new InvalidDataException("Empty name from project is not allowed")
        }

        try
        {
            projectService.register(name)
        }
        catch (NextVerException ex)
        {
            throw NextVerExceptionConverter.CastToException(ex)
        }
    }
}
