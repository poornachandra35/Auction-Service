package com.auction.notification_service.controller;

import com.auction.notification_service.dto.NotificationEvent;
import com.auction.notification_service.service.NotificationConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationConsumer notificationConsumer;

    @PostMapping
    public void sendNotification(@RequestBody NotificationEvent event) {
        notificationConsumer.consume(event);
    }
}