package com.asekulsk.nextver.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidDataException extends RuntimeException {
    InvalidDataException(String message) {
        super(message)
    }
}
