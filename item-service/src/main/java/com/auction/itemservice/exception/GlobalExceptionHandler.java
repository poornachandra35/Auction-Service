package com.auction.itemservice.exception;

import com.auction.itemservice.dto.ApiErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // =====================================================
    // VALIDATION EXCEPTION
    // =====================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse>
    handleValidationException(

            MethodArgumentNotValidException ex,

            HttpServletRequest request
    ) {

        log.error("Validation failed", ex);

        Map<String, String> validationErrors =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        validationErrors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ApiErrorResponse response =
                ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Validation Error")
                        .message(
                                "Invalid request payload"
                        )
                        .path(request.getRequestURI())
                        .validationErrors(validationErrors)
                        .build();

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // =====================================================
    // RESOURCE NOT FOUND
    // =====================================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse>
    handleResourceNotFound(

            ResourceNotFoundException ex,

            HttpServletRequest request
    ) {

        log.error("Resource not found", ex);

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    // =====================================================
    // BAD REQUEST
    // =====================================================

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse>
    handleBadRequest(

            BadRequestException ex,

            HttpServletRequest request
    ) {

        log.error("Bad request exception", ex);

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    // =====================================================
    // UNAUTHORIZED
    // =====================================================

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse>
    handleUnauthorized(

            UnauthorizedException ex,

            HttpServletRequest request
    ) {

        log.error("Unauthorized exception", ex);

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                request
        );
    }

    // =====================================================
    // GENERIC EXCEPTION
    // =====================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse>
    handleGenericException(

            Exception ex,

            HttpServletRequest request
    ) {

        log.error("Unexpected exception occurred", ex);

        return buildErrorResponse(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    // =====================================================
    // COMMON RESPONSE BUILDER
    // =====================================================

    private ResponseEntity<ApiErrorResponse>
    buildErrorResponse(

            String message,

            HttpStatus status,

            HttpServletRequest request
    ) {

        ApiErrorResponse response =
                ApiErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(status.value())
                        .error(status.getReasonPhrase())
                        .message(message)
                        .path(request.getRequestURI())
                        .build();

        return new ResponseEntity<>(
                response,
                status
        );
    }
}