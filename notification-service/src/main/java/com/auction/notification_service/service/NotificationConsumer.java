package com.auction.notification_service.service;

import com.auction.notification_service.dto.NotificationEvent;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import io.github.resilience4j.retry.annotation.Retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer
implements NotificationService
{

    private final EmailService emailService;

    // =====================================================
    // CONSUME EVENT
    // =====================================================

    @CircuitBreaker(
            name = "emailService",
            fallbackMethod = "fallback"
    )
    @Retry(name = "emailService")
    public void consume(
            NotificationEvent event
    ) {

        log.info(
                "New notification received for user: {}",
                event.getUserId()
        );

        emailService.sendEmail(
                event.getEmail(),
                event.getMessage()
        );

        log.info(
                "Notification processed successfully for: {}",
                event.getEmail()
        );
    }

    // =====================================================
    // FALLBACK
    // =====================================================

    public void fallback(

            NotificationEvent event,

            Exception ex
    ) {

        log.error(
                "Notification service fallback triggered for email: {}",
                event.getEmail(),
                ex
        );
    }
}