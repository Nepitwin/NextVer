package com.asekulsk.nextver.controller

import com.asekulsk.nextver.exceptions.InvalidDataException
import com.asekulsk.nextver.interfaces.IProjectService
import com.asekulsk.nextver.persistence.Project
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
        projectService.getProjectByName(name)
    }

    @PostMapping("/register/{name}")
    boolean register(@PathVariable("name") String name) {
        if (name.isBlank() or name.isEmpty() or name.isAllWhitespace())
        {
            throw new InvalidDataException("Empty name from project is not allowed")
        }

        projectService.register(name)
    }
}
