package com.asekulsk.nextver.api.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import spock.lang.Specification

class DataNotFoundExceptionSpec extends Specification {

    def "should extend RuntimeException and keep the message"() {
        given:
        def message = "Data not found"

        when:
        def ex = new DataNotFoundException(message)

        then:
        ex instanceof RuntimeException
        ex.message == message
    }

    def "should be annotated with ResponseStatus NOT_FOUND"() {
        when:
        def annotation = DataNotFoundException.getAnnotation(ResponseStatus)

        then:
        annotation != null
        annotation.value() == HttpStatus.NOT_FOUND
    }
}
