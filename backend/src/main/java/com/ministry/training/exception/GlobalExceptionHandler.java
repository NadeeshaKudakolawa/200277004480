package com.ministry.training.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(
            DuplicateNominationException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleDuplicateNomination(
            DuplicateNominationException exception
    ) {

        return buildResponse(
                HttpStatus.CONFLICT,
                "DUPLICATE",
                exception.getMessage()
        );
    }


    @ExceptionHandler(
            DataIntegrityViolationException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleDatabaseConflict(
            DataIntegrityViolationException exception
    ) {

        return buildResponse(
                HttpStatus.CONFLICT,
                "CONFLICT",
                "The submitted record conflicts with an existing database record."
        );
    }


    @ExceptionHandler(
            RuntimeException.class
    )
    public ResponseEntity<Map<String, Object>>
    handleRuntimeException(
            RuntimeException exception
    ) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "ERROR",
                exception.getMessage()
        );
    }


    private ResponseEntity<Map<String, Object>>
    buildResponse(
            HttpStatus httpStatus,
            String status,
            String message
    ) {

        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "timestamp",
                LocalDateTime.now()
        );


        response.put(
                "status",
                status
        );


        response.put(
                "code",
                httpStatus.value()
        );


        response.put(
                "message",
                message
        );


        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }
}