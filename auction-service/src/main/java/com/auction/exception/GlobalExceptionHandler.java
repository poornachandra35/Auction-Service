package com.auction.exception;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // VALIDATION ERRORS
    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<?> handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        log.error(
                "Validation exception occurred",
                ex
        );

        Map<String, String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    // RESOURCE NOT FOUND
    @ExceptionHandler(
            ResourceNotFoundException.class
    )
    public ResponseEntity<?> handleNotFound(
            ResourceNotFoundException ex
    ) {

        log.error(
                "Resource not found: {}",
                ex.getMessage()
        );

        return buildResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    // BAD REQUEST
    @ExceptionHandler(
            BadRequestException.class
    )
    public ResponseEntity<?> handleBadRequest(
            BadRequestException ex
    ) {

        log.error(
                "Bad request: {}",
                ex.getMessage()
        );

        return buildResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    // GENERIC EXCEPTION
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(
            Exception ex
    ) {

        log.error(
                "Internal server error",
                ex
        );

        return buildResponse(
                "Something went wrong: "
                        + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<?> buildResponse(
            String message,
            HttpStatus status
    ) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        response.put(
                "status",
                status.value()
        );

        response.put(
                "message",
                message
        );

        return new ResponseEntity<>(
                response,
                status
        );
    }
}