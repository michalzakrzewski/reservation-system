package com.zakrzewski.reservationsystem.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(final String message) {
        super(message);
    }
}
