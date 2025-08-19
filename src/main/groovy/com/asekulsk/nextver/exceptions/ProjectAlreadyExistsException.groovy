package com.asekulsk.nextver.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value= HttpStatus.ALREADY_REPORTED)
class ProjectAlreadyExistsException extends RuntimeException {
    ProjectAlreadyExistsException(String message) {
        super(message)
    }
}
