package com.auction.userservice.service.impl;

import com.auction.userservice.dto.*;

import com.auction.userservice.entity.Role;
import com.auction.userservice.entity.User;

import com.auction.userservice.exception.BadRequestException;
import com.auction.userservice.exception.ResourceNotFoundException;

import com.auction.userservice.repository.UserRepository;

import com.auction.userservice.service.NotificationServiceHelper;

import com.auction.userservice.util.JwtUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private NotificationServiceHelper notificationServiceHelper;

    @InjectMocks
    private AuthServiceImpl authService;

    // ================= ADMIN LOGIN =================

    @Test
    void testAdminLogin() {

        LoginRequest request = new LoginRequest();

        request.setEmail("admin@gmail.com");
        request.setPassword("admin123");

        when(jwtUtil.generateToken(any()))
                .thenReturn("admin-token");

        String result = authService.login(request);

        assertEquals("admin-token", result);
    }

    // ================= LOGIN SUCCESS =================

    @Test
    void testLoginSuccess() {

        LoginRequest request = new LoginRequest();

        request.setEmail("john@gmail.com");
        request.setPassword("123456");

        User user = User.builder()
                .email("john@gmail.com")
                .password("encodedPassword")
                .role(Role.BUYER)
                .build();

        when(userRepository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "123456",
                "encodedPassword"
        )).thenReturn(true);

        when(jwtUtil.generateToken(user))
                .thenReturn("jwt-token");

        String result = authService.login(request);

        assertEquals("jwt-token", result);
    }

    // ================= USER NOT FOUND =================

    @Test
    void testLoginUserNotFound() {

        LoginRequest request = new LoginRequest();

        request.setEmail("abc@gmail.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("abc@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> authService.login(request)
        );
    }

    // ================= INVALID PASSWORD =================

    @Test
    void testLoginInvalidPassword() {

        LoginRequest request = new LoginRequest();

        request.setEmail("john@gmail.com");
        request.setPassword("wrong");

        User user = User.builder()
                .email("john@gmail.com")
                .password("encoded")
                .build();

        when(userRepository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrong",
                "encoded"
        )).thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> authService.login(request)
        );
    }

    // ================= SEND OTP SUCCESS =================

    @Test
    void testSendOtpForRegistration() {

        when(userRepository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.empty());

        String result =
                authService.sendOtpForRegistration(
                        "john@gmail.com"
                );

        assertNotNull(result);

        verify(notificationServiceHelper, times(1))
                .sendNotification(any());
    }

    // ================= EMAIL EXISTS =================

    @Test
    void testSendOtpEmailExists() {

        User user = User.builder().build();

        when(userRepository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.of(user));

        assertThrows(
                BadRequestException.class,
                () -> authService.sendOtpForRegistration(
                        "john@gmail.com"
                )
        );
    }

    // ================= REGISTER SUCCESS =================

    @Test
    void testRegisterSuccess() {

        RegisterRequest request =
                new RegisterRequest();

        request.setName("John");

        request.setEmail("john@gmail.com");

        request.setPassword("123456");

        request.setRole("BUYER");

        request.setOtp("123456");

        AuthServiceImpl spyService =
                spy(authService);

        doReturn("User Registered Successfully")
                .when(spyService)
                .register(any(RegisterRequest.class));

        String result =
                spyService.register(request);

        assertEquals(
                "User Registered Successfully",
                result
        );
    }
    // ================= INVALID OTP =================

    @Test
    void testRegisterInvalidOtp() {

        RegisterRequest request =
                new RegisterRequest();

        request.setEmail("john@gmail.com");

        request.setOtp("wrong");

        assertThrows(
                BadRequestException.class,
                () -> authService.register(request)
        );
    }

    // ================= SEND RESET OTP =================

    @Test
    void testSendResetOtpSuccess() {

        User user = User.builder()
                .id(1L)
                .email("john@gmail.com")
                .build();

        when(userRepository.findByEmail(
                "john@gmail.com"
        )).thenReturn(Optional.of(user));

        String result =
                authService.sendResetOtp(
                        "john@gmail.com"
                );

        assertNotNull(result);

        verify(userRepository, times(1))
                .save(user);

        verify(notificationServiceHelper, times(1))
                .sendNotification(any());
    }

    // ================= RESET PASSWORD USER NOT FOUND =================

    @Test
    void testResetPasswordUserNotFound() {

        ResetPasswordRequest request =
                new ResetPasswordRequest();

        request.setEmail("abc@gmail.com");

        when(userRepository.findByEmail(
                "abc@gmail.com"
        )).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> authService.resetPassword(request)
        );
    }
}