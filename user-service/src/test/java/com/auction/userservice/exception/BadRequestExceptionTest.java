package com.auction.userservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {

    @Test
    void testBadRequestExceptionMessage() {
        // Arrange
        String message = "Invalid input";

        // Act
        BadRequestException ex = new BadRequestException(message);

        // Assert
        assertEquals(message, ex.getMessage());
        assertTrue(ex instanceof RuntimeException);
    }
}