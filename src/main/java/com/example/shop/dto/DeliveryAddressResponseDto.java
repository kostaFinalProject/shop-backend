package com.example.shop.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DeliveryAddressResponseDto {
    private Long deliveryAddressId;
    private Long memberId;
    private String postcode;
    private String roadAddress;
    private String detailAddress;

    public static DeliveryAddressResponseDto createDto(Long deliveryAddressId, Long memberId, String postcode, String roadAddress, String detailAddress) {
        return DeliveryAddressResponseDto.builder().deliveryAddressId(deliveryAddressId).memberId(memberId).postcode(postcode)
                .roadAddress(roadAddress).detailAddress(detailAddress).build();
    }
}
