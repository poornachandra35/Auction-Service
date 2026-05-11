package com.auction.service;

import com.auction.client.NotificationClient;

import com.auction.dto.NotificationRequest;

import com.auction.service.impl.NotificationService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl
        implements NotificationService {

    private final NotificationClient
            notificationClient;

    @Override
    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @CircuitBreaker(
            name = "notificationService",
            fallbackMethod = "notificationFallback"
    )
    public void sendAuctionWinnerNotification(
            Long userId
    ) {

        log.info(
                "Sending winner notification to userId: {}",
                userId
        );

        NotificationRequest request =
                new NotificationRequest();

        request.setUserId(userId);

        request.setMessage(
                "Congratulations! You won the auction. " +
                "Please complete payment."
        );

        notificationClient.sendNotification(request);

        log.info(
                "Notification sent successfully to userId: {}",
                userId
        );
    }

    // FALLBACK METHOD
    public void notificationFallback(
            Long userId,
            Exception ex
    ) {

        log.error(
                "Notification service fallback triggered for userId: {}",
                userId,
                ex
        );
    }
}