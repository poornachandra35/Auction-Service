package com.auction.userservice.service;

import com.auction.userservice.dto.*;
import com.auction.userservice.entity.Role;
import com.auction.userservice.entity.User;
import com.auction.userservice.exception.BadRequestException;
import com.auction.userservice.exception.ResourceNotFoundException;
import com.auction.userservice.repository.UserRepository;
import com.auction.userservice.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final NotificationServiceHelper notificationServiceHelper; // ✅ USE THIS
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // OTP storage
    private final Map<String, String> tempOtpStore = new ConcurrentHashMap<>();
    private final Map<String, Long> otpExpiryStore = new ConcurrentHashMap<>();

    // ================= REGISTER =================
    public String register(RegisterRequest request) {

        String storedOtp = tempOtpStore.get(request.getEmail());
        Long expiry = otpExpiryStore.get(request.getEmail());

        if (storedOtp == null || !storedOtp.equals(request.getOtp())) {
            throw new BadRequestException("Invalid OTP");
        }

        if (expiry == null || expiry < System.currentTimeMillis()) {
            throw new BadRequestException("OTP expired");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.valueOf(request.getRole()))
                .preferredCategories(request.getPreferredCategories())
                .minBudget(request.getMinBudget())
                .maxBudget(request.getMaxBudget())
                .location(request.getLocation())
                .build();

        userRepository.save(user);

        tempOtpStore.remove(request.getEmail());
        otpExpiryStore.remove(request.getEmail());

        return "User Registered Successfully";
    }

    // ================= LOGIN =================
    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        return jwtUtil.generateToken(user);
    }

    // ================= FORGOT PASSWORD =================
    public String sendResetOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String otp = generateOtp();

        user.setOtp(otp);
        user.setOtpExpiry(System.currentTimeMillis() + 5 * 60 * 1000);
        userRepository.save(user);

        // ✅ CORRECT CALL
        notificationServiceHelper.sendNotification(
                new NotificationEvent(
                        user.getId().toString(),
                        "Your OTP is: " + otp,
                        email
                )
        );

        return "OTP generated, but email service is currently unavailable if you didn’t receive it";
    }

    // ================= RESET PASSWORD =================
    public String resetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!request.getOtp().equals(user.getOtp())) {
            throw new BadRequestException("Invalid OTP");
        }

        if (user.getOtpExpiry() < System.currentTimeMillis()) {
            throw new BadRequestException("OTP expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);

        return "Password reset successful";
    }

    // ================= REGISTRATION OTP =================
    public String sendOtpForRegistration(String email) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        String otp = generateOtp();

        tempOtpStore.put(email, otp);
        otpExpiryStore.put(email, System.currentTimeMillis() + 5 * 60 * 1000);

        // ✅ CORRECT CALL
        notificationServiceHelper.sendNotification(
                new NotificationEvent(
                        null,
                        "Your Registration OTP is: " + otp,
                        email
                )
        );

        return "OTP generated, but email service is currently unavailable if you didn’t receive it";
    }

    // ================= HELPER =================
    private String generateOtp() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }
}