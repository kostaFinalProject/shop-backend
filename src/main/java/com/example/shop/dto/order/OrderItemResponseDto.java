package com.example.shop.dto.order;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OrderItemResponseDto {
    private Long itemId;
    private String itemName;
    private String itemSize;
    private int itemPrice;
    private String itemRepImgUrl;

    public static OrderItemResponseDto createDto(Long itemId, String itemName, String itemSize,
                                                int itemPrice, String itemRepImgUrl) {

        return OrderItemResponseDto.builder().itemId(itemId).itemName(itemName)
                .itemSize(itemSize).itemPrice(itemPrice).itemRepImgUrl(itemRepImgUrl).build();
    }
}
