package com.auction.userservice.service.impl;

import com.auction.userservice.dto.*;

import com.auction.userservice.entity.Role;
import com.auction.userservice.entity.User;

import com.auction.userservice.exception.BadRequestException;
import com.auction.userservice.exception.ResourceNotFoundException;

import com.auction.userservice.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // ================= GET USER SUCCESS =================

    @Test
    void testGetUserById() {

        User user = User.builder()
                .id(1L)
                .name("John")
                .email("john@gmail.com")
                .role(Role.BUYER)
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponseDto response =
                userService.getUserById(1L);

        assertEquals("John", response.getName());
    }

    // ================= USER NOT FOUND =================

    @Test
    void testGetUserByIdNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(1L)
        );
    }

    // ================= SAVE PREFERENCES SUCCESS =================

    @Test
    void testSavePreferences() {

        User user = User.builder()
                .id(1L)
                .role(Role.BUYER)
                .build();

        UserPreferenceRequestDto request =
                new UserPreferenceRequestDto();

        request.setPreferredCategories(
                Set.of("Electronics")
        );

        request.setMinBudget(100.0);

        request.setMaxBudget(1000.0);

        request.setLocation("India");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserPreferenceResponseDto response =
                userService.savePreferences(1L, request);

        assertEquals("India", response.getLocation());

        verify(userRepository, times(1))
                .save(user);
    }

    // ================= NON BUYER =================

    @Test
    void testSavePreferencesNonBuyer() {

        User user = User.builder()
                .role(Role.SELLER)
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        assertThrows(
                BadRequestException.class,
                () -> userService.savePreferences(
                        1L,
                        new UserPreferenceRequestDto()
                )
        );
    }

    // ================= GET PREFERENCES =================

    @Test
    void testGetPreferences() {

        User user = User.builder()
                .id(1L)
                .email("john@gmail.com")
                .location("India")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserPreferenceResponseDto response =
                userService.getPreferences(1L);

        assertEquals("India", response.getLocation());
    }

    // ================= FILTER BUYERS SUCCESS =================

    @Test
    void testFilterBuyers() {

        User user = User.builder()
                .id(1L)
                .role(Role.BUYER)
                .preferredCategories(
                        Set.of("Electronics")
                )
                .minBudget(100.0)
                .maxBudget(1000.0)
                .location("India")
                .build();

        when(userRepository.findByRole(Role.BUYER))
                .thenReturn(List.of(user));

        List<UserPreferenceResponseDto> result =
                userService.filterBuyers(
                        "Electronics",
                        200.0,
                        500.0,
                        "India"
                );

        assertEquals(1, result.size());
    }

    // ================= FILTER NO MATCH =================

    @Test
    void testFilterBuyersNoMatch() {

        User user = User.builder()
                .role(Role.BUYER)
                .preferredCategories(Set.of("Fashion"))
                .location("USA")
                .minBudget(1000.0)
                .maxBudget(2000.0)
                .build();

        when(userRepository.findByRole(Role.BUYER))
                .thenReturn(List.of(user));

        List<UserPreferenceResponseDto> result =
                userService.filterBuyers(
                        "Electronics",
                        10.0,
                        20.0,
                        "India"
                );

        assertEquals(0, result.size());
    }

    // ================= GET BUYERS =================

    @Test
    void testGetAllBuyers() {

        User user = User.builder()
                .id(1L)
                .name("John")
                .email("john@gmail.com")
                .role(Role.BUYER)
                .build();

        when(userRepository.findByRole(Role.BUYER))
                .thenReturn(List.of(user));

        List<UserResponseDto> buyers =
                userService.getAllBuyers();

        assertEquals(1, buyers.size());
    }
}