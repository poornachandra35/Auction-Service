package com.auction.userservice.service;

import com.auction.userservice.dto.*;
import com.auction.userservice.entity.*;
import com.auction.userservice.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserById() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .email("john@gmail.com")
                .role(Role.BUYER)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDto dto = userService.getUserById(1L);

        assertEquals("John", dto.getName());
    }

    @Test
    void testSavePreferences() {
        User user = User.builder()
                .id(1L)
                .role(Role.BUYER)
                .build();

        UserPreferenceRequestDto request = new UserPreferenceRequestDto();
        request.setLocation("India");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserPreferenceResponseDto response = userService.savePreferences(1L, request);

        assertEquals("India", response.getLocation());
    }

    @Test
    void testFilterBuyers() {
        User user = User.builder()
                .id(1L)
                .role(Role.BUYER)
                .preferredCategories(Set.of("Electronics"))
                .minBudget(100.0)
                .maxBudget(1000.0)
                .location("India")
                .build();

        when(userRepository.findByRole(Role.BUYER))
                .thenReturn(List.of(user));

        List<UserPreferenceResponseDto> result =
                userService.filterBuyers("Electronics", 200.0, 500.0, "India");

        assertFalse(result.isEmpty());
    }
}