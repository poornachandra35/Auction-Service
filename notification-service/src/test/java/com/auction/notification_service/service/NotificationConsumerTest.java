package com.auction.notification_service.service;

import com.auction.notification_service.dto.NotificationEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    @Test
    void testConsume_SendsEmail() {

        NotificationEvent event = new NotificationEvent();
        event.setUserId("1");
        event.setEmail("user@example.com");
        event.setMessage("Auction won!");

        notificationConsumer.consume(event);

        verify(emailService, times(1))
                .sendEmail("user@example.com", "Auction won!");
    }

    @Test
    void testConsume_NullEmail() {

        NotificationEvent event = new NotificationEvent();
        event.setUserId("2");
        event.setEmail(null);
        event.setMessage("Test message");

        notificationConsumer.consume(event);

        verify(emailService, times(1))
                .sendEmail(null, "Test message");
    }

    @Test
    void testConsume_EmptyMessage() {

        NotificationEvent event = new NotificationEvent();
        event.setUserId("3");
        event.setEmail("user@test.com");
        event.setMessage("");

        notificationConsumer.consume(event);

        verify(emailService, times(1))
                .sendEmail("user@test.com", "");
    }
}