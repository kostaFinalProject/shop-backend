package com.example.shop.dto.deliveryaddress;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DeliveryAddressResponseDto {
    private Long deliveryAddressId;
    private String postCode;
    private String roadAddress;
    private String detailAddress;

    public static DeliveryAddressResponseDto createDto(Long deliveryAddressId, String postCode,
                                                       String roadAddress, String detailAddress) {

        return DeliveryAddressResponseDto.builder().deliveryAddressId(deliveryAddressId)
                .postCode(postCode).roadAddress(roadAddress).detailAddress(detailAddress).build();
    }
}
