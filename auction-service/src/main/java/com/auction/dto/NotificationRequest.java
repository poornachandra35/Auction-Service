package com.auction.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class NotificationRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Message cannot be empty")
    private String message;
}