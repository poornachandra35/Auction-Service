package com.auction.userservice.controller;

import com.auction.userservice.dto.*;
import com.auction.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        String response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    // ================= SEND OTP (REGISTER) =================
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@Valid @RequestBody ForgotPasswordRequest request) {
        String response = authService.sendOtpForRegistration(request.getEmail());
        return ResponseEntity.ok(response);
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String response = authService.sendResetOtp(request.getEmail());
        return ResponseEntity.ok(response);
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        String response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}