package com.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopSellerDto {

    private String name;

    private Long totalAuctions;

    private Double revenue;
}