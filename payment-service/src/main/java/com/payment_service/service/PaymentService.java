package com.payment_service.service;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentVerificationRequest;
import com.payment_service.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse createOrder(
            PaymentRequest request
    ) throws Exception;
    void verifyPayment(
            PaymentVerificationRequest request
    );
    
}