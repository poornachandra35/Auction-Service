package com.auction.userservice.util;

import com.auction.userservice.entity.Role;
import com.auction.userservice.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private final String SECRET = "mysecretkeymysecretkeymysecretkey12345"; // 🔥 must be >= 32 chars

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();

        // ✅ Inject secret manually (since @Value won't work here)
        Field secretField = JwtUtil.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtUtil, SECRET);
    }

    @Test
    void testGenerateToken() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .email("john@gmail.com")
                .name("John")
                .role(Role.BUYER)
                .build();

        // Act
        String token = jwtUtil.generateToken(user);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testTokenClaims() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .email("john@gmail.com")
                .name("John")
                .role(Role.BUYER)
                .build();

        String token = jwtUtil.generateToken(user);

        // Act: Parse token
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Assert
        assertEquals("john@gmail.com", claims.getSubject());
        assertEquals(1, claims.get("userId"));
        assertEquals("BUYER", claims.get("role"));
        assertEquals("John", claims.get("name"));

        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }
}