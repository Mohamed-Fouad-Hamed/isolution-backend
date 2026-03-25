package com.alf.security.common.exceptions;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {
    private  final HttpStatus httpStatus;
    public ApplicationException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
