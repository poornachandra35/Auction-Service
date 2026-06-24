package com.auction.userservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForgotPasswordRequestTest {

    @Test
    void testForgotPasswordRequest() {

        ForgotPasswordRequest request =
                new ForgotPasswordRequest();

        request.setEmail("john@gmail.com");

        assertEquals(
                "john@gmail.com",
                request.getEmail()
        );

        assertNotNull(request.toString());

        assertEquals(request, request);

        assertNotEquals(request, null);

        assertNotEquals(request, new Object());
    }
}