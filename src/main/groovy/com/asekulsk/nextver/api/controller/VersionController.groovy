package com.asekulsk.nextver.api.controller

import com.asekulsk.nextver.api.request.NextVersionRequest
import com.asekulsk.nextver.api.request.RegisterVersionRequest
import com.asekulsk.nextver.api.util.NextVerExceptionConverter
import com.asekulsk.nextver.domain.exceptions.NextVerException
import com.asekulsk.nextver.persistence.interfaces.IVersionService
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
        try
        {
            versionService.getNextVersion(project, body.versionName, body.versionIncrementType).version
        }
        catch (NextVerException ex)
        {
            throw NextVerExceptionConverter.CastToException(ex)
        }
    }

    @PostMapping("/register/{project}")
    boolean register(@PathVariable("project") String project, @RequestBody RegisterVersionRequest body) {
        try
        {
            versionService.register(project, body.versionName, body.version, body.type)
        }
        catch (NextVerException ex)
        {
            throw NextVerExceptionConverter.CastToException(ex)
        }
    }
}
