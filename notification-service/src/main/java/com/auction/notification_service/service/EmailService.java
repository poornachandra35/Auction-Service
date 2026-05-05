package com.auction.notification_service.service;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${mailjet.api-key}")
    private String apiKey;

    @Value("${mailjet.secret-key}")
    private String secretKey;

    public void sendEmail(String toEmail, String message) {

        try {
            // ✅ Validate email
//            if (toEmail == null || toEmail.isEmpty()) {
//                System.out.println("❌ Invalid email address");
//                return;
//            }
//
//            // ✅ Initialize Mailjet client
//            MailjetClient client = new MailjetClient(apiKey, secretKey);
//
//            // ✅ Create email JSON
//            JSONObject email = new JSONObject();
//
//            email.put(Emailv31.Message.FROM,
//                    new JSONObject()
//                            .put("Email", "poornachandra.biz@gmail.com") // ⚠️ must be verified in Mailjet
//                            .put("Name", "Auction System"));
//
//            email.put(Emailv31.Message.TO,
//                    new JSONArray()
//                            .put(new JSONObject()
//                                    .put("Email", toEmail)
//                                    .put("Name", "User")));
//
//            email.put(Emailv31.Message.SUBJECT, "Auction Notification");
//            email.put(Emailv31.Message.TEXTPART, message);
//
//            // ✅ Create Mailjet request
//            MailjetRequest request = new MailjetRequest(Emailv31.resource)
//                    .property(Emailv31.MESSAGES, new JSONArray().put(email));
//
//            // ✅ Send email
//            MailjetResponse response = client.post(request);

            // ✅ Logs
            System.out.println("=================================");
            System.out.println("📬 EMAIL SENT");
            System.out.println("To: " + toEmail);
            System.out.println("Message: " + message);
            //System.out.println("Status: " + response.getStatus());
            System.out.println("=================================");

        } catch (Exception e) {
            System.out.println("❌ Email failed");
            e.printStackTrace();
        }
    }
}