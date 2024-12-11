package com.example.shop.dto.login;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;

    @Builder
    private LoginResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponseDto createLoginResponseDto(String accessToken, String refreshToken) {
        return LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
}
