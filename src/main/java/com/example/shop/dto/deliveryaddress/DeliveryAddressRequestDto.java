package com.example.shop.dto.deliveryaddress;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeliveryAddressRequestDto {
    private String postCode;
    private String roadAddress;
    private String detailAddress;
}
