package com.example.shop.dto.order;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OrderResponseDto {
    private Long paymentsId;
    List<OrderItemResponseDto> orderItems;
    private int orderPrice;
    private String orderStatus;
    private LocalDateTime orderTime;

    public static OrderResponseDto createDto(Long paymentsId, List<OrderItemResponseDto> orderItems,
                                             int orderPrice, String orderStatus, LocalDateTime orderTime) {
        return OrderResponseDto.builder().paymentsId(paymentsId).orderItems(orderItems)
                .orderPrice(orderPrice).orderStatus(orderStatus).orderTime(orderTime).build();
    }
}
