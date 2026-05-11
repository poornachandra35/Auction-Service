package com.auction.userservice.service;

import com.auction.userservice.dto.*;

import java.util.List;

public interface UserService {

    UserResponseDto getUserById(Long id);

    UserPreferenceResponseDto savePreferences(
            Long userId,
            UserPreferenceRequestDto dto
    );

    UserPreferenceResponseDto getPreferences(
            Long userId
    );

    List<UserPreferenceResponseDto> filterBuyers(
            String category,
            Double minPrice,
            Double maxPrice,
            String location
    );

    List<UserResponseDto> getAllBuyers();
}