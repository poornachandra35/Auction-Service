package com.payment_service.service.impl;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import com.payment_service.dto.PaymentVerificationRequest;

import com.payment_service.entity.Payment;

import com.payment_service.repository.PaymentRepository;

import com.payment_service.service.PaymentService;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.payment_service.client.AuctionClient;
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl
        implements PaymentService {

    private final PaymentRepository
            paymentRepository;
    private final AuctionClient
    auctionClient;
    @Value("${razorpay.key.id}")
    private String razorpayKey;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    // =====================================
    // CREATE ORDER
    // =====================================

    @Override
    public PaymentResponse createOrder(
            PaymentRequest request
    ) throws Exception {

        RazorpayClient razorpay =
                new RazorpayClient(
                        razorpayKey,
                        razorpaySecret
                );

        JSONObject orderRequest =
                new JSONObject();

        orderRequest.put(
                "amount",
                request.getAmount() * 100
        );

        orderRequest.put(
                "currency",
                "INR"
        );

        orderRequest.put(
                "receipt",
                "txn_" + System.currentTimeMillis()
        );

        Order order =
                razorpay.orders.create(
                        orderRequest
                );

        log.info(
                "Razorpay order created: {}",
                order.toString()
        );

        PaymentResponse response =
                PaymentResponse.builder()

                        .orderId(
                                order.get("id")
                                        .toString()
                        )

                        .amount(
                                Double.parseDouble(
                                        order.get("amount")
                                                .toString()
                                )
                        )

                        .currency(
                                order.get("currency")
                                        .toString()
                        )

                        .status(
                                order.get("status")
                                        .toString()
                        )

                        .razorpayKey(
                                razorpayKey
                        )

                        .build();

        return response;
    }

    // =====================================
    // VERIFY PAYMENT
    // =====================================

    @Override
    public void verifyPayment(
            PaymentVerificationRequest request
    ) {

        try {

            // =====================================
            // VERIFY SIGNATURE
            // =====================================

            JSONObject options =
                    new JSONObject();

            options.put(
                    "razorpay_order_id",
                    request.getRazorpayOrderId()
            );

            options.put(
                    "razorpay_payment_id",
                    request.getRazorpayPaymentId()
            );

            options.put(
                    "razorpay_signature",
                    request.getRazorpaySignature()
            );

            boolean isValid =
                    Utils.verifyPaymentSignature(
                            options,
                            razorpaySecret
                    );

            if (!isValid) {

                throw new RuntimeException(
                        "Invalid Payment Signature"
                );
            }

            log.info(
                    "Payment Signature Verified"
            );

            // =====================================
            // SAVE PAYMENT
            // =====================================

            Payment payment =
                    new Payment();

            payment.setAuctionId(
                    request.getAuctionId()
            );

            payment.setWinnerId(
                    request.getWinnerId()
            );

            payment.setRazorpayPaymentId(
                    request.getRazorpayPaymentId()
            );

            payment.setRazorpayOrderId(
                    request.getRazorpayOrderId()
            );

            payment.setRazorpaySignature(
                    request.getRazorpaySignature()
            );

            payment.setPaymentStatus(
                    "SUCCESS"
            );

            paymentRepository.save(
                    payment
            );

            log.info(
                    "Payment saved successfully"
            );
            auctionClient.paymentSuccess(
                    request.getAuctionId()
            );

            log.info(
                    "Auction status updated successfully"
            );

        } catch (Exception e) {

            log.error(
                    "Payment Verification Failed",
                    e
            );

            throw new RuntimeException(
                    "Payment Verification Failed"
            );
        }
    }
}