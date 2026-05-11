package com.auction.notification_service.service;

public interface EmailService {

    void sendEmail(
            String toEmail,
            String message
    );
}