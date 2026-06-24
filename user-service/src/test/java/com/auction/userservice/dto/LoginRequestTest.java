package com.auction.userservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testLoginRequest() {

        LoginRequest request1 = new LoginRequest();

        request1.setEmail("john@gmail.com");
        request1.setPassword("123456");

        LoginRequest request2 = new LoginRequest();

        request2.setEmail("john@gmail.com");
        request2.setPassword("123456");

        // getters

        assertEquals(
                "john@gmail.com",
                request1.getEmail()
        );

        assertEquals(
                "123456",
                request1.getPassword()
        );

        // toString

        assertNotNull(request1.toString());

        // equals

        assertEquals(request1, request1);

        // hashcode

        assertEquals(
                request1.hashCode(),
                request1.hashCode()
        );

        assertNotEquals(request1, null);

        assertNotEquals(request1, new Object());

        assertNotEquals(request1, request2);
    }
}