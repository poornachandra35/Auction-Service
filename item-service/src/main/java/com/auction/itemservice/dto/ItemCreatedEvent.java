package com.auction.itemservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCreatedEvent {

    private Long itemId;

    private String title;

    private String category;

    private Double basePrice;
}