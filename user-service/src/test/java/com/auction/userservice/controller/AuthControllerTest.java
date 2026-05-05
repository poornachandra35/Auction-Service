package com.auction.userservice.controller;

import com.auction.userservice.dto.*;
import com.auction.userservice.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ REGISTER
    @Test
    void testRegister() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("John");
        request.setEmail("john@gmail.com");
        request.setPassword("123");
        request.setRole("BUYER");
        request.setOtp("123456");

        when(authService.register(request))
                .thenReturn("User Registered Successfully");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Registered Successfully"));
    }

    // ✅ LOGIN
    @Test
    void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@gmail.com");
        request.setPassword("123");

        when(authService.login(request)).thenReturn("token");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("token"));
    }

    // ✅ SEND OTP (REGISTER)
    @Test
    void testSendOtp() throws Exception {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("john@gmail.com");

        when(authService.sendOtpForRegistration("john@gmail.com"))
                .thenReturn("OTP sent");

        mockMvc.perform(post("/api/auth/send-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // ✅ FORGOT PASSWORD
    @Test
    void testForgotPassword() throws Exception {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("john@gmail.com");

        when(authService.sendResetOtp("john@gmail.com"))
                .thenReturn("OTP sent");

        mockMvc.perform(post("/api/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // ✅ RESET PASSWORD
    @Test
    void testResetPassword() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("john@gmail.com");
        request.setOtp("123456");
        request.setNewPassword("newpass");

        when(authService.resetPassword(request))
                .thenReturn("Password reset successful");

        mockMvc.perform(post("/api/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}