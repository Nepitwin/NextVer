package com.asekulsk.nextver.api.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value= HttpStatus.ALREADY_REPORTED)
class DataAlreadyExistsException extends RuntimeException {
    DataAlreadyExistsException(String message) {
        super(message)
    }
}
