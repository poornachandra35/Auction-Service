package com.auction.userservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testResourceNotFoundExceptionMessage() {
        // Arrange
        String message = "User not found";

        // Act
        ResourceNotFoundException ex = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, ex.getMessage());
        assertTrue(ex instanceof RuntimeException);
    }
}