package com.example.shop.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryAddressDto {
    private String postcode;
    private String roadAddress;
    private String detailAddress;
}
