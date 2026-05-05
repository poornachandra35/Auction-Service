package com.auction.userservice.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    // ✅ TEST VALIDATION ERROR
    @Test
    void testHandleValidationErrors() {
        // Mock BindingResult
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        bindingResult.addError(new FieldError("object", "email", "Email is required"));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<Map<String, String>> response =
                handler.handleValidationErrors(ex);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().containsKey("email"));
        assertEquals("Email is required", response.getBody().get("email"));
    }

    // ✅ TEST RESOURCE NOT FOUND
    @Test
    void testHandleNotFound() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("User not found");

        ResponseEntity<String> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
    }

    // ✅ TEST BAD REQUEST
    @Test
    void testHandleBadRequest() {
        BadRequestException ex =
                new BadRequestException("Invalid request");

        ResponseEntity<String> response = handler.handleBadRequest(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid request", response.getBody());
    }

    // ✅ TEST GLOBAL EXCEPTION
    @Test
    void testHandleGlobalException() {
        Exception ex = new Exception("Something failed");

        ResponseEntity<String> response = handler.handleGlobal(ex);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Something went wrong", response.getBody());
    }
}