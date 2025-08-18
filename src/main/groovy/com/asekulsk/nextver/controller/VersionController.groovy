package com.asekulsk.nextver.controller

import com.asekulsk.nextver.enumeration.VersionIncrementType
import com.asekulsk.nextver.persistence.Version
import com.asekulsk.nextver.request.RegisterVersionRequest
import com.asekulsk.nextver.service.VersionService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/versions")
class VersionController {

    private final VersionService versionService

    VersionController(VersionService versionService) {
        this.versionService = versionService
    }

    @PostMapping("/{project}/next/{type}")
    Version next(
            @PathVariable("project") String project,
            @PathVariable("type") VersionIncrementType type
    ) {
        versionService.getNextVersion(project, type)
    }

    @PostMapping("/{project}/register")
    Version register(
            @PathVariable("project") String project,
            @RequestBody RegisterVersionRequest body
    ) {
        versionService.registerVersion(project, body.versionString, body.released)
    }
}
