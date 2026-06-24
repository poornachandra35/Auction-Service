package com.auction.userservice.dto;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserPreferenceRequestDtoTest {

    @Test
    void testUserPreferenceRequestDto() {

        UserPreferenceRequestDto dto =
                new UserPreferenceRequestDto();

        dto.setPreferredCategories(
                Set.of("Electronics")
        );

        dto.setMinBudget(100.0);

        dto.setMaxBudget(1000.0);

        dto.setLocation("India");

        assertEquals(
                "India",
                dto.getLocation()
        );

        assertEquals(
                100.0,
                dto.getMinBudget()
        );

        assertEquals(
                1000.0,
                dto.getMaxBudget()
        );

        assertNotNull(dto.toString());

        assertEquals(dto, dto);

        assertNotEquals(dto, null);

        assertNotEquals(dto, new Object());
    }
}