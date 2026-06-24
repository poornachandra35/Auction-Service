package com.auction.userservice.dto;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserPreferenceResponseDtoTest {

    @Test
    void testUserPreferenceResponseDto() {

        UserPreferenceResponseDto dto1 =
                UserPreferenceResponseDto.builder()

                .userId(1L)

                .email("john@gmail.com")

                .preferredCategories(
                        Set.of("Electronics")
                )

                .minBudget(100.0)

                .maxBudget(1000.0)

                .location("India")

                .build();

        UserPreferenceResponseDto dto2 =
                UserPreferenceResponseDto.builder()
                .userId(1L)
                .build();

        assertEquals(1L, dto1.getUserId());

        assertEquals(
                "john@gmail.com",
                dto1.getEmail()
        );

        assertEquals(
                "India",
                dto1.getLocation()
        );

        assertNotNull(dto1.toString());

        assertEquals(dto1, dto1);

        assertNotEquals(dto1, null);

        assertNotEquals(dto1, new Object());

        assertNotEquals(dto1, dto2);
    }
}