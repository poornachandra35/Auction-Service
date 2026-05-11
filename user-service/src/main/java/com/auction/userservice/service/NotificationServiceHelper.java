package com.auction.userservice.service;

import com.auction.userservice.client.NotificationClient;
import com.auction.userservice.dto.NotificationEvent;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceHelper {

    private final NotificationClient notificationClient;

    @CircuitBreaker(
            name = "notificationService",
            fallbackMethod = "fallback"
    )
    @Retry(name = "notificationService")
    public void sendNotification(
            NotificationEvent event
    ) {

        notificationClient.sendNotification(event);
    }

    public void fallback(

            NotificationEvent event,

            Exception ex
    ) {

        log.error(
                "Notification service DOWN for email: {}",
                event.getEmail(),
                ex
        );
    }
}