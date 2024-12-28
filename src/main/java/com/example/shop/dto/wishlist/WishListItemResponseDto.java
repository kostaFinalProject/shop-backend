package com.example.shop.dto.wishlist;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class WishListItemResponseDto {
    private Long wishListItemId;
    private Long itemId;
    private String manufacturer;
    private String itemName;
    private int itemPrice;
    private String itemSeller;
    private int points;
    private String itemRepImageUrl;

    public static WishListItemResponseDto createDto(Long wishListItemId, Long itemId, String manufacturer,
                                                    String itemName, int itemPrice, String itemSeller,
                                                    int points, String itemRepImageUrl) {

        return WishListItemResponseDto.builder().wishListItemId(wishListItemId).itemId(itemId)
                .manufacturer(manufacturer).itemName(itemName).itemPrice(itemPrice)
                .itemSeller(itemSeller).points(points).itemRepImageUrl(itemRepImageUrl).build();
    }
}
