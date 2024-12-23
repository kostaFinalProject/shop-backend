package com.example.shop.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemRequestDto {
    private Long itemSizeId;
    private int count;
}
