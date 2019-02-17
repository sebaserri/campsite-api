package com.campsite.controller.exception;

import java.io.Serializable;

public class ReservationException extends RuntimeException implements Serializable {

    public ReservationException() {
    }

    public ReservationException(String message) {
        super(message);
    }

    public ReservationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationException(Throwable cause) {
        super(cause);
    }
}
