package com.example.shop.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderRequestDto {
    private List<OrderItemRequestDto> orderItems = new ArrayList<>();
}
