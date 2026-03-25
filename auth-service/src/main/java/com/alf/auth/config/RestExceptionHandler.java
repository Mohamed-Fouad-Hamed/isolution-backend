package com.alf.auth.config;

import com.alf.core_common.dtos.error.ErrorDto;
import com.alf.security.common.exceptions.ApplicationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class RestExceptionHandler {

    public ResponseEntity<ErrorDto> handleException(ApplicationException ex){
        return  ResponseEntity.status(ex.getHttpStatus())
                .body(new ErrorDto(ex.getMessage()));

    }
}
