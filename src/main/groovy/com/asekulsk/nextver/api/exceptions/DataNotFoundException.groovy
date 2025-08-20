package com.asekulsk.nextver.api.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class DataNotFoundException extends RuntimeException {
    DataNotFoundException(String message) {
        super(message)
    }
}
