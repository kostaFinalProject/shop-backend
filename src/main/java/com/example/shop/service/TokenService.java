package com.example.shop.service;

import com.example.shop.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;

    public String generateAccessToken(String email) {
        return jwtUtil.generateToken(email);
    }

    public String generateRefreshToken(String email) {
        return jwtUtil.generateRefreshToken(email);
    }

    public boolean validateAccessToken(String token) {
        return jwtUtil.validateToken(token);
    }

    public void invalidateToken(String token) {
        jwtUtil.invalidateToken(token);
    }

    public String regenerateRefreshToken(String email) {
        return jwtUtil.regenerateRefreshToken(email);
    }

    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }

    public String refreshAccessToken(String refreshToken) {
        return jwtUtil.refreshAccessToken(refreshToken);
    }
}
