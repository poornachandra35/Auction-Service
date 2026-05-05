package com.auction.notification_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private String userId;
    private String message;
    private String email;
}