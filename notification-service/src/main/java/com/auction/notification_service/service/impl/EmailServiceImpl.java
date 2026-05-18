package com.auction.notification_service.service.impl;

import com.auction.notification_service.service.EmailService;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;

import com.mailjet.client.resource.Emailv31;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl
        implements EmailService {

    @Value("${mailjet.api-key}")
    private String apiKey;

    @Value("${mailjet.secret-key}")
    private String secretKey;

    // =====================================================
    // SEND EMAIL
    // =====================================================

    @Override
    public void sendEmail(

            String toEmail,

            String message
    ) {

        try {

            // =====================================================
            // VALIDATE EMAIL
            // =====================================================

            if (
                    toEmail == null
                    || toEmail.isBlank()
            ) {

                log.error(
                        "Invalid email address provided"
                );

                return;
            }

            log.info(
                    "Sending email to: {}",
                    toEmail
            );

            log.info(
                    "EMAIL CONTENT -> To: {} | Message: {}",
                    toEmail,
                    message
            );

            // =====================================================
            // INITIALIZE MAILJET CLIENT
            // =====================================================

            MailjetClient client =
                    new MailjetClient(
                            apiKey,
                            secretKey
                    );

            // =====================================================
            // CREATE EMAIL JSON
            // =====================================================

            JSONObject email =
                    new JSONObject();

            email.put(

                    Emailv31.Message.FROM,

                    new JSONObject()

                            .put(
                                    "Email",
                                    "poornachandra.biz@gmail.com"
                            )

                            .put(
                                    "Name",
                                    "Auction System"
                            )
            );

            email.put(

                    Emailv31.Message.TO,

                    new JSONArray()

                            .put(
                                    new JSONObject()

                                            .put(
                                                    "Email",
                                                    toEmail
                                            )

                                            .put(
                                                    "Name",
                                                    "User"
                                            )
                            )
            );

            email.put(
                    Emailv31.Message.SUBJECT,
                    "Auction Notification"
            );

            email.put(
                    Emailv31.Message.TEXTPART,
                    message
            );

            // =====================================================
            // CREATE REQUEST
            // =====================================================

            MailjetRequest request =

                    new MailjetRequest(
                            Emailv31.resource
                    )

                            .property(
                                    Emailv31.MESSAGES,
                                    new JSONArray()
                                            .put(email)
                            );

            // =====================================================
            // SEND EMAIL
            // =====================================================

            MailjetResponse response =
                    client.post(request);

            // =====================================================
            // SUCCESS LOGS
            // =====================================================

            log.info(
                    "Email sent successfully to: {}",
                    toEmail
            );

            log.info(
                    "Mailjet response status: {}",
                    response.getStatus()
            );

            log.debug(
                    "Mailjet response data: {}",
                    response.getData()
            );

        } catch (Exception ex) {

            // =====================================================
            // ERROR LOGGING
            // =====================================================

            log.error(
                    "Email sending failed for: {}",
                    toEmail,
                    ex
            );

            throw new RuntimeException(
                    "Failed to send email",
                    ex
            );
        }
    }
}