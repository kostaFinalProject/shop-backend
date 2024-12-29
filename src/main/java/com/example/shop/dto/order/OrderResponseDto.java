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
    private Long orderId;
    List<OrderItemResponseDto> orderItems;
    private int orderPrice;
    private int usePoints;
    private String orderStatus;
    private LocalDateTime orderTime;

    public static OrderResponseDto createDto(Long paymentsId, Long orderId, List<OrderItemResponseDto> orderItems,
                                             int orderPrice, int usePoints, String orderStatus, LocalDateTime orderTime) {
        return OrderResponseDto.builder().paymentsId(paymentsId).orderId(orderId).orderItems(orderItems)
                .orderPrice(orderPrice).usePoints(usePoints).orderStatus(orderStatus).orderTime(orderTime).build();
    }
}
