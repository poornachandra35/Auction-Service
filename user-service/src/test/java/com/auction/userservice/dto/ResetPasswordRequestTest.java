package com.auction.userservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResetPasswordRequestTest {

    @Test
    void testResetPasswordRequest() {

        ResetPasswordRequest request =
                new ResetPasswordRequest();

        request.setEmail("john@gmail.com");

        request.setOtp("123456");

        request.setNewPassword("newpassword");

        assertEquals(
                "john@gmail.com",
                request.getEmail()
        );

        assertEquals(
                "123456",
                request.getOtp()
        );

        assertEquals(
                "newpassword",
                request.getNewPassword()
        );

        assertNotNull(request.toString());

        assertEquals(request, request);

        assertNotEquals(request, null);

        assertNotEquals(request, new Object());
    }
}