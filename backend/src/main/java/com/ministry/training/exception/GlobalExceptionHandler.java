package com.ministry.training.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateNominationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicate(
            DuplicateNominationException exception
    ) {

        Map<String, String> response =
                new LinkedHashMap<>();

        response.put(
                "status",
                "DUPLICATE"
        );

        response.put(
                "message",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException exception
    ) {

        String message =
                exception
                        .getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error ->
                                error.getDefaultMessage()
                        )
                        .distinct()
                        .collect(
                                Collectors.joining(" ")
                        );

        Map<String, String> response =
                new LinkedHashMap<>();

        response.put(
                "status",
                "VALIDATION_ERROR"
        );

        response.put(
                "message",
                message
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }
}