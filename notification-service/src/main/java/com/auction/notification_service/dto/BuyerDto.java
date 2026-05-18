package com.auction.notification_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyerDto {

    private Long userId;

    private String name;

    private String email;

    private String location;
}