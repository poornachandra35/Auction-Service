package com.auction.notification_service.service;

import com.auction.notification_service.dto.NotificationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProcessorService {

    private final EmailService emailService;

    public void processOtpNotification(
            NotificationEvent event
    ) {

        log.info(
                "Processing OTP notification for: {}",
                event.getEmail()
        );

        emailService.sendEmail(
                event.getEmail(),
                event.getMessage()
        );

        log.info(
                "OTP email sent successfully"
        );
    }

    public void processAuctionNotification(
            String email,
            String message
    ) {

        log.info(
                "Processing auction notification for: {}",
                email
        );

        emailService.sendEmail(
                email,
                message
        );

        log.info(
                "Auction notification sent successfully"
        );
    }

    public void processItemNotification(
            String email,
            String message
    ) {

        log.info(
                "Processing item notification for: {}",
                email
        );

        emailService.sendEmail(
                email,
                message
        );

        log.info(
                "Item notification sent successfully"
        );
    }
}