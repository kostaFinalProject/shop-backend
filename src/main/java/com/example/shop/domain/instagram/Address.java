package com.example.shop.domain.instagram;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {

    private String postCode;
    private String roadAddress;
    private String detailAddress;

    @Builder
    private Address(String postCode, String roadAddress, String detailAddress) {
        this.postCode = postCode;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
    }

    public static Address createAddress(String postCode, String roadAddress, String detailAddress) {
        return Address.builder().postCode(postCode).roadAddress(roadAddress).detailAddress(detailAddress).build();
    }
}
