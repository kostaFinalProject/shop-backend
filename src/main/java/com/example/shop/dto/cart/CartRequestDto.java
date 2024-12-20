package com.example.shop.dto.cart;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartRequestDto {
    private Long itemId;
    private String size;
    private int quantity;
}
