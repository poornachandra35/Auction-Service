package com.auction.notification_service.service;

import com.auction.notification_service.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;  // ✅ inject bean

    public void consume(NotificationEvent event) {

        System.out.println("=================================");
        System.out.println("🔔 NEW NOTIFICATION");
        System.out.println("User: " + event.getUserId());
        System.out.println("Message: " + event.getMessage());
        System.out.println("=================================");

        // ✅ correct way (non-static call)
        emailService.sendEmail(
                event.getEmail(),
                event.getMessage()
        );
    }
}