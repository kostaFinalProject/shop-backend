package com.example.shop.dto.order;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OrderItemResponseDto {
    private Long itemId;
    private Long itemSizeId;
    private String itemName;
    private String itemManufacturer;
    private String itemSeller;
    private String itemSize;
    private int quantity;
    private int itemPrice;
    private String itemStatus;
    private String itemRepImgUrl;

    public static OrderItemResponseDto createDto(Long itemId, Long itemSizeId, String itemName, String itemManufacturer,
                                                 String itemSeller, String itemSize, int quantity, int itemPrice,
                                                 String itemStatus, String itemRepImgUrl) {

        return OrderItemResponseDto.builder().itemId(itemId).itemSizeId(itemSizeId).itemName(itemName)
                .itemManufacturer(itemManufacturer).itemSeller(itemSeller).itemSize(itemSize)
                .quantity(quantity).itemPrice(itemPrice).itemStatus(itemStatus).itemRepImgUrl(itemRepImgUrl).build();
    }
}
