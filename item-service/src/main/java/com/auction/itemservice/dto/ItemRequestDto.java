package com.auction.itemservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequestDto {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String category;

    @NotNull
    private Double basePrice;

    @NotNull
    private Integer auctionDurationMinutes;

    @NotNull
    private LocalDateTime auctionStartTime; // ✅ NEW

	public LocalDateTime getAuctionStartTime() {
		// TODO Auto-generated method stub
		return auctionStartTime;
	}
}

