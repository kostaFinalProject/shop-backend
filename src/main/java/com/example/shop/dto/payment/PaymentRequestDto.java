package com.example.shop.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentRequestDto {
    private String impUid;
    private Long orderId;
    private int orderPrice;
    private int usePoints;
}
