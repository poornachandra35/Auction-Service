package com.auction.notification_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCreatedEvent {

    private Long itemId;

    private String title;

    private String category;

    private Double basePrice;
}