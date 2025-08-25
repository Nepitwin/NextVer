package com.asekulsk.nextver.unit.domain.exceptions

import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.exceptions.NextVerException
import spock.lang.Specification

class NextVerExceptionSpec extends Specification {

    def "should extend RuntimeException and keep message and reason"() {
        given:
        def message = "Data already exists"
        def reason = NextVerReason.DataAlreadyExists

        when:
        def ex = new NextVerException(message, reason)

        then:
        ex instanceof RuntimeException
        ex.message == message
        ex.reason == reason
    }

    def "should support all NextVerReason enum values"() {
        when:
        def ex = new NextVerException("Error with reason ${reason}", reason)

        then:
        ex.reason == reason
        ex.message == "Error with reason ${reason}"

        where:
        reason << [
                NextVerReason.DataAlreadyExists,
                NextVerReason.DataNotFound,
                NextVerReason.InvalidData,
                NextVerReason.PersistFailed
        ]
    }
}
