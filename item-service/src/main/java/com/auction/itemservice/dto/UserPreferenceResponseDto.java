package com.auction.itemservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferenceResponseDto {

    private Long userId;
    private String category;
    private Double minPrice;
    private Double maxPrice;
    private String location;
    private String email;
	
}