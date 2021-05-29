package com.znaczek.users.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.Annotation;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Annotation[] annotations = ex.getClass().getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof ResponseStatus) {
                httpStatus = ((ResponseStatus) annotation).value();
            }
        }
        log.error("Exception handled:", ex);
        return ResponseEntity
                .status(httpStatus)
                .body(new ErrorResponse(ex.getMessage(), System.currentTimeMillis())
                );
    }

}