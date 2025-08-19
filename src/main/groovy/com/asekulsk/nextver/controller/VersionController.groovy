package com.asekulsk.nextver.controller

import com.asekulsk.nextver.interfaces.IVersionService
import com.asekulsk.nextver.request.NextVersionRequest
import com.asekulsk.nextver.request.RegisterVersionRequest
import org.springframework.web.bind.annotation.GetMapping
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

    @GetMapping("/next/{project}")
    String next(@PathVariable("project") String project, @RequestBody NextVersionRequest body) {
        versionService.getNextVersion(project, body.versionName, body.versionIncrementType).version
    }

    @PostMapping("/register/{project}")
    boolean register(@PathVariable("project") String project, @RequestBody RegisterVersionRequest body) {
        versionService.register(project, body.versionName, body.version, body.type)
    }
}
