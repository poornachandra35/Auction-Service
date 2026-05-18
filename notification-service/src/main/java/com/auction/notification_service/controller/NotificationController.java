package com.auction.notification_service.controller;

import com.auction.notification_service.dto.NotificationEvent;

import com.auction.notification_service.service.NotificationProcessorService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationProcessorService
            notificationProcessorService;

    @PostMapping
    public void sendNotification(
            @RequestBody NotificationEvent event
    ) {

        notificationProcessorService
                .processOtpNotification(event);
    }
}