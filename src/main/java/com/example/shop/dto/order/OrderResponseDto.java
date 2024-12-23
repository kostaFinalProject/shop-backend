package com.example.shop.dto.order;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OrderResponseDto {
    List<OrderItemResponseDto> orderItems;
    private int orderPrice;
    private String orderStatus;

    public static OrderResponseDto createDto(List<OrderItemResponseDto> orderItems, int orderPrice, String orderStatus) {
        return OrderResponseDto.builder().orderItems(orderItems).orderPrice(orderPrice).orderStatus(orderStatus).build();
    }
}
