package com.asekulsk.nextver.unit.api.util

import com.asekulsk.nextver.api.exceptions.DataNotFoundException
import com.asekulsk.nextver.api.exceptions.InvalidDataException
import com.asekulsk.nextver.api.exceptions.DataAlreadyExistsException
import com.asekulsk.nextver.api.exceptions.UnknownException
import com.asekulsk.nextver.api.util.NextVerExceptionConverter
import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.exceptions.NextVerException
import spock.lang.Specification

class NextVerExceptionConverterSpec extends Specification {

    def "should convert NextVerException with reason=InvalidData to InvalidDataException"() {
        given:
        def ex = new NextVerException("bad data", NextVerReason.InvalidData)

        when:
        def result = NextVerExceptionConverter.CastToException(ex)

        then:
        result instanceof InvalidDataException
        result.message == "bad data"
    }

    def "should convert NextVerException with reason=DataAlreadyExists to DataAlreadyExistsException"() {
        given:
        def ex = new NextVerException("already exists", NextVerReason.DataAlreadyExists)

        when:
        def result = NextVerExceptionConverter.CastToException(ex)

        then:
        result instanceof DataAlreadyExistsException
        result.message == "already exists"
    }

    def "should convert NextVerException with reason=DataNotFound to DataNotFoundException"() {
        given:
        def ex = new NextVerException("missing data", NextVerReason.DataNotFound)

        when:
        def result = NextVerExceptionConverter.CastToException(ex)

        then:
        result instanceof DataNotFoundException
        result.message == "missing data"
    }

    def "should convert NextVerException with unknown reason to UnknownException"() {
        given:
        def ex = new NextVerException("unexpected", null) // or some unmapped enum if possible

        when:
        def result = NextVerExceptionConverter.CastToException(ex)

        then:
        result instanceof UnknownException
        result.message == "Unsupported exception found"
    }
}
