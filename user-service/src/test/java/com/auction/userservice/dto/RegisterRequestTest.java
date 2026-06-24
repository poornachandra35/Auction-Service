package com.auction.userservice.dto;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    @Test
    void testRegisterRequest() {

        RegisterRequest request =
                new RegisterRequest();

        request.setName("John");

        request.setEmail("john@gmail.com");

        request.setPassword("123456");

        request.setPhone("9876543210");

        request.setRole("BUYER");

        request.setPreferredCategories(
                Set.of("Electronics")
        );

        request.setMinBudget(100.0);

        request.setMaxBudget(1000.0);

        request.setLocation("India");

        request.setOtp("123456");

        assertEquals("John", request.getName());

        assertEquals(
                "john@gmail.com",
                request.getEmail()
        );

        assertEquals(
                "123456",
                request.getPassword()
        );

        assertEquals(
                "9876543210",
                request.getPhone()
        );

        assertEquals(
                "BUYER",
                request.getRole()
        );

        assertEquals(
                "India",
                request.getLocation()
        );

        assertEquals(
                "123456",
                request.getOtp()
        );

        assertNotNull(request.toString());

        assertEquals(request, request);

        assertNotEquals(request, null);

        assertNotEquals(request, new Object());
    }
}