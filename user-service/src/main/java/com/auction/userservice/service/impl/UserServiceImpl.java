package com.auction.userservice.service.impl;

import com.auction.userservice.dto.UserPreferenceRequestDto;
import com.auction.userservice.dto.UserPreferenceResponseDto;
import com.auction.userservice.dto.UserResponseDto;

import com.auction.userservice.entity.Role;
import com.auction.userservice.entity.User;

import com.auction.userservice.exception.BadRequestException;
import com.auction.userservice.exception.ResourceNotFoundException;

import com.auction.userservice.repository.UserRepository;

import com.auction.userservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl
        implements UserService {

    private final UserRepository userRepository;

    // =====================================================
    // GET USER BY ID
    // =====================================================

    @Override
    public UserResponseDto getUserById(
            Long id
    ) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    // =====================================================
    // SAVE PREFERENCES
    // =====================================================

    @Override
    public UserPreferenceResponseDto savePreferences(

            Long userId,

            UserPreferenceRequestDto dto
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        if (user.getRole() != Role.BUYER) {

            throw new BadRequestException(
                    "Only buyers can set preferences"
            );
        }

        user.setPreferredCategories(
                dto.getPreferredCategories()
        );

        user.setMinBudget(
                dto.getMinBudget()
        );

        user.setMaxBudget(
                dto.getMaxBudget()
        );

        user.setLocation(
                dto.getLocation()
        );

        userRepository.save(user);

        log.info(
                "Preferences updated for user: {}",
                userId
        );

        return mapToResponse(user);
    }

    // =====================================================
    // GET PREFERENCES
    // =====================================================

    @Override
    public UserPreferenceResponseDto getPreferences(
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        return mapToResponse(user);
    }

    // =====================================================
    // FILTER BUYERS
    // =====================================================

    @Override
    public List<UserPreferenceResponseDto> filterBuyers(

            String category,

            Double minPrice,

            Double maxPrice,

            String location
    ) {

        List<User> buyers =
                userRepository.findByRole(
                        Role.BUYER
                );

        return buyers.stream()

                .filter(user ->
                        matchCategory(
                                user.getPreferredCategories(),
                                category
                        )
                )

                .filter(user ->
                        matchBudget(
                                user.getMinBudget(),
                                user.getMaxBudget(),
                                minPrice,
                                maxPrice
                        )
                )

                .filter(user ->
                        matchLocation(
                                user.getLocation(),
                                location
                        )
                )

                .map(this::mapToResponse)

                .collect(Collectors.toList());
    }

    // =====================================================
    // GET ALL BUYERS
    // =====================================================

    @Override
    public List<UserResponseDto> getAllBuyers() {

        List<User> buyers =
                userRepository.findByRole(
                        Role.BUYER
                );

        return buyers.stream()

                .map(user ->
                        UserResponseDto.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .role(user.getRole().name())
                                .build()
                )

                .toList();
    }

    // =====================================================
    // HELPER METHODS
    // =====================================================

    private boolean matchCategory(

            Set<String> categories,

            String category
    ) {

        return categories != null
                && categories.contains(category);
    }

    private boolean matchBudget(

            Double userMin,

            Double userMax,

            Double itemMin,

            Double itemMax
    ) {

        if (userMin == null || userMax == null) {
            return true;
        }

        return itemMax >= userMin
                && itemMin <= userMax;
    }

    private boolean matchLocation(

            String userLocation,

            String itemLocation
    ) {

        if (userLocation == null
                || itemLocation == null) {

            return true;
        }

        return userLocation.equalsIgnoreCase(
                itemLocation
        );
    }

    private UserPreferenceResponseDto mapToResponse(
            User user
    ) {

        return UserPreferenceResponseDto.builder()

                .userId(user.getId())

                .email(user.getEmail())

                .preferredCategories(
                        user.getPreferredCategories()
                )

                .minBudget(user.getMinBudget())

                .maxBudget(user.getMaxBudget())

                .location(user.getLocation())

                .build();
    }
}