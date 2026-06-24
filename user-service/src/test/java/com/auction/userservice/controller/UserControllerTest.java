package com.auction.userservice.controller;

import com.auction.userservice.dto.*;

import com.auction.userservice.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= GET USER =================

    @Test
    void testGetUserById() throws Exception {

        UserResponseDto dto = UserResponseDto.builder()
                .id(1L)
                .name("John")
                .email("john@gmail.com")
                .role("BUYER")
                .build();

        when(userService.getUserById(1L))
                .thenReturn(dto);

        mockMvc.perform(get("/api/users/1"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email")
                .value("john@gmail.com"));
    }

    // ================= SAVE PREFERENCES =================

    @Test
    void testSavePreferences() throws Exception {

        UserPreferenceRequestDto request =
                new UserPreferenceRequestDto();

        UserPreferenceResponseDto response =
                UserPreferenceResponseDto.builder()
                .userId(1L)
                .location("India")
                .build();

        when(userService.savePreferences(1L, request))
                .thenReturn(response);

        mockMvc.perform(post("/api/users/preferences")
                .header("userId", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.location")
                .value("India"));
    }

    // ================= GET PREFERENCES =================

    @Test
    void testGetPreferences() throws Exception {

        UserPreferenceResponseDto response =
                UserPreferenceResponseDto.builder()
                .userId(1L)
                .location("India")
                .build();

        when(userService.getPreferences(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/users/1/preferences"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.location")
                .value("India"));
    }

    // ================= GET BUYERS =================

    @Test
    void testGetAllBuyers() throws Exception {

        when(userService.getAllBuyers())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/users/buyers"))

                .andExpect(status().isOk());
    }

    // ================= FILTER BUYERS =================

    @Test
    void testFilterBuyers() throws Exception {

        when(userService.filterBuyers(
                "Electronics",
                100.0,
                1000.0,
                "India"))

                .thenReturn(List.of());

        mockMvc.perform(get("/api/users/filter")
                .param("category", "Electronics")
                .param("minPrice", "100")
                .param("maxPrice", "1000")
                .param("location", "India"))

                .andExpect(status().isOk());
    }
}