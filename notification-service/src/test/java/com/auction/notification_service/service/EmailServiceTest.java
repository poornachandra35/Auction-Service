package com.auction.notification_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmailServiceTest {

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService();

        // Inject dummy API keys
        ReflectionTestUtils.setField(emailService, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(emailService, "secretKey", "test-secret-key");
    }

    @Test
    void testSendEmail_Success() {
        assertDoesNotThrow(() -> {
            emailService.sendEmail("test@example.com", "Test message");
        });
    }

    @Test
    void testSendEmail_InvalidEmail() {
        assertDoesNotThrow(() -> {
            emailService.sendEmail("", "Test message");
        });
    }

    @Test
    void testSendEmail_NullEmail() {
        assertDoesNotThrow(() -> {
            emailService.sendEmail(null, "Test message");
        });
    }
}