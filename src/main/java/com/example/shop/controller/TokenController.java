package com.example.shop.controller;

import com.example.shop.aop.TokenApi;
import com.example.shop.dto.login.RefreshTokenResponse;
import com.example.shop.dto.login.TokenValidationResponse;
import com.example.shop.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tokens")
public class TokenController {

    private final TokenService tokenService;

    @TokenApi
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        boolean isValid = tokenService.validateAccessToken(jwtToken);
        return ResponseEntity.ok(TokenValidationResponse.createDto(isValid));
    }

    @TokenApi
    @PostMapping("/refresh")
    public ResponseEntity<?> regenerateRefreshToken(@RequestHeader("Authorization") String refreshToken) {
        String jwtRefreshToken = refreshToken.replace("Bearer ", "");
        String email = tokenService.extractUsername(jwtRefreshToken);
        String newRefreshToken = tokenService.regenerateRefreshToken(email);
        return ResponseEntity.ok(RefreshTokenResponse.createDto("Bearer " + newRefreshToken));
    }
}
