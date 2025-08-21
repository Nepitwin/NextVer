package com.asekulsk.nextver.api.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import spock.lang.Specification

class DataAlreadyExistsExceptionSpec extends Specification {

    def "should extend RuntimeException and keep the message"() {
        given:
        def message = "Data already exists"

        when:
        def ex = new DataAlreadyExistsException(message)

        then:
        ex instanceof RuntimeException
        ex.message == message
    }

    def "should be annotated with ResponseStatus ALREADY_REPORTED"() {
        when:
        def annotation = DataAlreadyExistsException.getAnnotation(ResponseStatus)

        then:
        annotation != null
        annotation.value() == HttpStatus.ALREADY_REPORTED
    }
}
