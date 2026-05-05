package com.auction.userservice.service;

import com.auction.userservice.client.NotificationClient;
import com.auction.userservice.dto.NotificationEvent;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceHelper {

    private final NotificationClient notificationClient;

    @CircuitBreaker(name = "notificationService", fallbackMethod = "fallback")
    @Retry(name = "notificationService")
    public void sendNotification(NotificationEvent event) {
        notificationClient.sendNotification(event);
    }

    public void fallback(NotificationEvent event, Exception ex) {
        System.out.println("⚠ Notification service DOWN: " + ex.getMessage());
        System.out.println("Fallback executed. Email not sent to: " + event.getEmail());
    }
}