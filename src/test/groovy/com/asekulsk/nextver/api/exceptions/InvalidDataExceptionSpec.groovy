package com.asekulsk.nextver.api.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import spock.lang.Specification

class InvalidDataExceptionSpec extends Specification {

    def "should extend RuntimeException and keep the message"() {
        given:
        def message = "Invalid input provided"

        when:
        def ex = new InvalidDataException(message)

        then:
        ex instanceof RuntimeException
        ex.message == message
    }

    def "should be annotated with ResponseStatus BAD_REQUEST"() {
        when:
        def annotation = InvalidDataException.getAnnotation(ResponseStatus)

        then:
        annotation != null
        annotation.value() == HttpStatus.BAD_REQUEST
    }
}
