package com.asekulsk.nextver.controller

import com.asekulsk.nextver.enumeration.VersionIncrementType
import com.asekulsk.nextver.interfaces.IVersionService
import com.asekulsk.nextver.persistence.Version
import com.asekulsk.nextver.request.RegisterVersionRequest
import com.asekulsk.nextver.service.VersionService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/version")
class VersionController {

    private final IVersionService versionService

    VersionController(IVersionService versionService) {
        this.versionService = versionService
    }

    @PostMapping("/next/{project}")
    Version next(@PathVariable("project") String project, @PathVariable("type") VersionIncrementType type) {
        versionService.getNextVersion(project, type)
    }

    @PostMapping("/register/{project}")
    boolean register(@PathVariable("project") String project, @RequestBody RegisterVersionRequest body) {
        versionService.register(project, body.versionName, body.version, body.type)
    }
}
