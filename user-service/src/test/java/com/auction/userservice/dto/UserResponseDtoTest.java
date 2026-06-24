package com.auction.userservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDtoTest {

    @Test
    void testUserResponseDto() {

        UserResponseDto dto1 =
                UserResponseDto.builder()

                .id(1L)

                .name("John")

                .email("john@gmail.com")

                .role("BUYER")

                .build();

        UserResponseDto dto2 =
                UserResponseDto.builder()
                .id(2L)
                .build();

        assertEquals(1L, dto1.getId());

        assertEquals(
                "John",
                dto1.getName()
        );

        assertEquals(
                "john@gmail.com",
                dto1.getEmail()
        );

        assertEquals(
                "BUYER",
                dto1.getRole()
        );

        assertNotNull(dto1.toString());

        assertEquals(dto1, dto1);

        assertNotEquals(dto1, null);

        assertNotEquals(dto1, new Object());

        assertNotEquals(dto1, dto2);
    }
}