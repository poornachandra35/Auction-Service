package com.auction.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class NotificationEvent {
    private String userId;
    private String message;
    private String email;
}