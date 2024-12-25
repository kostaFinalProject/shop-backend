package com.example.shop.dto.wishlist;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class WishListItemResponseDto {
    private Long wishListItemId;
    private Long itemId;
    private String itemName;
    private int itemPrice;
    private int mileage;
    private String itemRepImageUrl;

    public static WishListItemResponseDto createDto(Long wishListItemId, Long itemId, String itemName,
                                                    int itemPrice, String itemRepImageUrl) {

        return WishListItemResponseDto.builder().wishListItemId(wishListItemId).itemId(itemId)
                .itemName(itemName).itemPrice(itemPrice).itemRepImageUrl(itemRepImageUrl).build();
    }
}
