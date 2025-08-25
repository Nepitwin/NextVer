package com.asekulsk.nextver.api.util

import com.asekulsk.nextver.api.exceptions.DataNotFoundException
import com.asekulsk.nextver.api.exceptions.InvalidDataException
import com.asekulsk.nextver.api.exceptions.DataAlreadyExistsException
import com.asekulsk.nextver.api.exceptions.PersistFailedException
import com.asekulsk.nextver.api.exceptions.UnknownException
import com.asekulsk.nextver.domain.enumeration.NextVerReason
import com.asekulsk.nextver.domain.exceptions.NextVerException

class NextVerExceptionConverter {

    static Exception CastToException(NextVerException exception)
    {
        switch (exception.reason)
        {
            case NextVerReason.InvalidData:
                return new InvalidDataException(exception.message)
            case NextVerReason.DataAlreadyExists:
                return new DataAlreadyExistsException(exception.message)
            case NextVerReason.DataNotFound:
                return new DataNotFoundException(exception.message)
            case NextVerReason.PersistFailed:
                return new PersistFailedException(exception.message)
            default:
                return new UnknownException("Unsupported exception found")
        }
    }
}
