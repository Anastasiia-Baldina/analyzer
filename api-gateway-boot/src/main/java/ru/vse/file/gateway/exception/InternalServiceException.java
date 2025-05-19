package ru.vse.file.gateway.exception;

import org.springframework.http.HttpStatus;

public class InternalServiceException extends RuntimeException {
    private final HttpStatus httpStatus;

    public InternalServiceException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public InternalServiceException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
