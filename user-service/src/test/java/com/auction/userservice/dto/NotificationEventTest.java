package com.auction.userservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationEventTest {

    @Test
    void testNotificationEventBuilder() {

        NotificationEvent event1 =
                NotificationEvent.builder()
                .userId("1")
                .email("john@gmail.com")
                .message("Test Message")
                .build();

        NotificationEvent event2 =
                new NotificationEvent(
                        "1",
                        "john@gmail.com",
                        "Test Message"
                );

        assertEquals("1", event1.getUserId());

        assertEquals(
                "john@gmail.com",
                event1.getEmail()
        );

        assertEquals(
                "Test Message",
                event1.getMessage()
        );

        assertNotNull(event1.toString());

        assertEquals(event1, event1);

        assertNotEquals(event1, null);

        assertNotEquals(event1, new Object());

        assertEquals(
                event2.getEmail(),
                event1.getEmail()
        );
    }
}