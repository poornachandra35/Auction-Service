package com.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopBuyerDto {

    private String name;

    private Long totalBids;

    private Long wonAuctions;
}