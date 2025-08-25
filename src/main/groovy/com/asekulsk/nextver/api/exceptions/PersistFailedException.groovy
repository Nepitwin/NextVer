package com.asekulsk.nextver.api.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_MODIFIED)
class PersistFailedException extends RuntimeException {
    PersistFailedException(String message) {
        super(message)
    }
}
