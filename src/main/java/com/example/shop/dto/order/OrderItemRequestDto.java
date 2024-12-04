package com.example.shop.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemRequestDto {
    private Long itemId;
    private String size;
    private int count;
}
