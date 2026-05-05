package com.auction.userservice.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Set;

@Data
public class UserPreferenceRequestDto {

    private Set<String> preferredCategories;

    @Positive(message = "Min budget must be positive")
    private Double minBudget;

    @Positive(message = "Max budget must be positive")
    private Double maxBudget;

    private String location;
}