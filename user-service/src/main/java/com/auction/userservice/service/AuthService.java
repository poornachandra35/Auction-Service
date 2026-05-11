package com.auction.userservice.service;

import com.auction.userservice.dto.*;

public interface AuthService {

    String register(RegisterRequest request);

    String login(LoginRequest request);

    String sendOtpForRegistration(String email);

    String sendResetOtp(String email);

    String resetPassword(
            ResetPasswordRequest request
    );
}