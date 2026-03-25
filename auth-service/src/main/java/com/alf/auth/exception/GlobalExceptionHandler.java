package com.alf.auth.exception;

import com.alf.core_common.dtos.payload.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageResponse> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse(ex.getMessage(), 400, 400, null, null));
    }
}
