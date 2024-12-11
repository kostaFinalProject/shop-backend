package com.example.shop.dto.login;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenResponse {
    private String newToken;

    @Builder
    private RefreshTokenResponse(String newToken) {
        this.newToken = newToken;
    }

    public static RefreshTokenResponse createDto(String newToken) {
        return RefreshTokenResponse.builder().newToken(newToken).build();
    }
}
