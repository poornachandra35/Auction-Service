package com.auction.userservice.util;
import com.auction.userservice.entity.User;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ✅ Generate Token with Expiry
    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getEmail()) // email
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .claim("name", user.getName()) // ✅ ADD THIS LINE
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}