package com.asekulsk.nextver.domain.exceptions

import com.asekulsk.nextver.domain.enumeration.NextVerReason

class NextVerException extends RuntimeException {

    NextVerReason reason

    NextVerException(String message, NextVerReason reason) {
        super(message)
        this.reason = reason
    }
}
