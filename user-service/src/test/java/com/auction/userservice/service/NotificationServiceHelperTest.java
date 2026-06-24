package com.auction.userservice.service;

import com.auction.userservice.client.NotificationClient;
import com.auction.userservice.dto.NotificationEvent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceHelperTest {

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private NotificationServiceHelper notificationServiceHelper;

    // ================= SUCCESS CASE =================

    @Test
    void testSendNotificationSuccess() {

        NotificationEvent event =
                NotificationEvent.builder()
                .email("john@gmail.com")
                .message("Test Notification")
                .build();

        doNothing()
                .when(notificationClient)
                .sendNotification(event);

        assertDoesNotThrow(() ->
                notificationServiceHelper
                .sendNotification(event));

        verify(notificationClient, times(1))
                .sendNotification(event);
    }

    // ================= FAILURE CASE =================

    @Test
    void testSendNotificationFailure() {

        NotificationEvent event =
                NotificationEvent.builder()
                .email("john@gmail.com")
                .message("Test Notification")
                .build();

        doThrow(new RuntimeException("Service Down"))
                .when(notificationClient)
                .sendNotification(event);

        try {

            notificationServiceHelper
                    .sendNotification(event);

        } catch (Exception ignored) {

        }

        verify(notificationClient, times(1))
                .sendNotification(event);
    }

    // ================= FALLBACK METHOD =================

    @Test
    void testFallbackMethod() {

        NotificationEvent event =
                NotificationEvent.builder()
                .email("john@gmail.com")
                .message("Fallback Test")
                .build();

        assertDoesNotThrow(() ->

                notificationServiceHelper.fallback(

                        event,

                        new RuntimeException("Service Down")
                )
        );
    }
}