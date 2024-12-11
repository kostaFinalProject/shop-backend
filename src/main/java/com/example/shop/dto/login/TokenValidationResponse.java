package com.example.shop.dto.login;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenValidationResponse {
    private boolean valid;

    @Builder
    private TokenValidationResponse(boolean valid) {
        this.valid = valid;
    }

    public static TokenValidationResponse createDto(boolean valid) {
        return TokenValidationResponse.builder().valid(valid).build();
    }
}
