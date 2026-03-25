package com.alf.security.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Assume ResourceNotFoundException exists from previous example

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class DataConflictException extends RuntimeException {
    public DataConflictException(String message) {
        super(message);
    }
}