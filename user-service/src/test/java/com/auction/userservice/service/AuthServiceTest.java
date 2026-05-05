package com.auction.userservice.service;

import com.auction.userservice.dto.*;
import com.auction.userservice.entity.User;
import com.auction.userservice.repository.UserRepository;
import com.auction.userservice.util.JwtUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private NotificationServiceHelper notificationServiceHelper;

    @InjectMocks
    private AuthService authService;

    // ✅ LOGIN SUCCESS
    @Test
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@gmail.com");
        request.setPassword("123");

        User user = User.builder()
                .email("john@gmail.com")
                .password("encoded")
                .build();

        when(userRepository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123", "encoded")).thenReturn(true);

        when(jwtUtil.generateToken(user)).thenReturn("token");

        String result = authService.login(request);

        assertEquals("token", result);
    }

    // ✅ LOGIN FAIL (WRONG PASSWORD)
    @Test
    void testLoginFail() {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@gmail.com");
        request.setPassword("wrong");

        User user = User.builder()
                .email("john@gmail.com")
                .password("encoded")
                .build();

        when(userRepository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> authService.login(request));
    }
}