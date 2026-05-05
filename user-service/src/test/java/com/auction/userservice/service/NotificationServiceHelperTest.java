package com.auction.userservice.service;

import com.auction.userservice.client.NotificationClient;
import com.auction.userservice.dto.NotificationEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

class NotificationServiceHelperTest {

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private NotificationServiceHelper helper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ SUCCESS CASE
    @Test
    void testSendNotification_success() {
        // Arrange
        NotificationEvent event = new NotificationEvent(
                "1",
                "Test message",
                "test@gmail.com"
        );

        // Act
        helper.sendNotification(event);

        // Assert
        verify(notificationClient, times(1))
                .sendNotification(event);
    }

    // ✅ FAILURE CASE (simulate exception)
    @Test
    void testSendNotification_failure() {
        // Arrange
        NotificationEvent event = new NotificationEvent(
                "1",
                "Test message",
                "test@gmail.com"
        );

        doThrow(new RuntimeException("Service down"))
                .when(notificationClient)
                .sendNotification(event);

        // Act + Assert
        try {
            helper.sendNotification(event);
        } catch (Exception ignored) {
            // Exception expected since fallback won't trigger in unit test
        }

        verify(notificationClient, times(1))
                .sendNotification(event);
    }

    // ✅ TEST FALLBACK METHOD DIRECTLY
    @Test
    void testFallback() {
        // Arrange
        NotificationEvent event = new NotificationEvent(
                "1",
                "Test message",
                "test@gmail.com"
        );

        Exception ex = new RuntimeException("Service down");

        // Act
        helper.fallback(event, ex);

        // Assert
        // No exception should be thrown
        // (Fallback just prints logs)
    }
}