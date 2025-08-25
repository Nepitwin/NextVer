package com.asekulsk.nextver.unit.api.util

import com.asekulsk.nextver.api.exceptions.DataNotFoundException
import com.asekulsk.nextver.api.exceptions.InvalidDataException
import com.asekulsk.nextver.api.exceptions.DataAlreadyExistsException
import com.asekulsk.nextver.api.exceptions.UnknownException
import com.asekulsk.nextver.api.exceptions.PersistFailedException
import com.asekulsk.nextver.api.util.NextVerExceptionConverter
import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.exceptions.NextVerException
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import spock.lang.Specification

class NextVerExceptionConverterSpec extends Specification {

    def "should convert NextVerException"() {
        given:
        def ex = new NextVerException(msg, reason as NextVerReason)

        when:
        def result = NextVerExceptionConverter.CastToException(ex)

        then:
        result.getClass().is(expectedException)
        result.message == expectedMsg
        resolveStatus(result) == expectedHttpStatus

        where:
        msg              | reason                          || expectedException          || expectedMsg                   || expectedHttpStatus
        "bad data"       | NextVerReason.InvalidData       || InvalidDataException       || "bad data"                    || HttpStatus.BAD_REQUEST
        "already exists" | NextVerReason.DataAlreadyExists || DataAlreadyExistsException || "already exists"              || HttpStatus.ALREADY_REPORTED
        "missing data"   | NextVerReason.DataNotFound      || DataNotFoundException      || "missing data"                || HttpStatus.NOT_FOUND
        "persist failed" | NextVerReason.PersistFailed     || PersistFailedException     || "persist failed"              || HttpStatus.NOT_MODIFIED
        "unexpected"     | null                            || UnknownException           || "Unsupported exception found" || HttpStatus.BAD_REQUEST
    }

    private static HttpStatus resolveStatus(Throwable ex) {
        ResponseStatus responseStatus = AnnotatedElementUtils.findMergedAnnotation(
                ex.getClass(), ResponseStatus.class
        )
        return (responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
