package com.auction.userservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserPreferenceResponseDto {

    private Long userId;
    private String email;

    private Set<String> preferredCategories;

    private Double minBudget;

    private Double maxBudget;

    private String location;
    

}