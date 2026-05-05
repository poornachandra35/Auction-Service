package com.auction.notification_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyerDto {
    private Long id;
    private String name;
    private String email;
    private String location;
}