package com.example.shop.dto.cart;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemDto {
    private Long cartId;
    private Long itemId;
    private Long itemSizeId;
    private String size;
    private String name;
    private int price;
    private String repImgUrl;
    private int count;
    private int currentPrice;
    private String manufacturer;
    private String seller;

    @Builder
    private CartItemDto(Long cartId, Long itemId, Long itemSizeId, String size, String name, int price,
                        String repImgUrl, int count, int currentPrice, String manufacturer, String seller) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.itemSizeId = itemSizeId;
        this.size = size;
        this.name = name;
        this.price = price;
        this.repImgUrl = repImgUrl;
        this.count = count;
        this.currentPrice = currentPrice;
        this.manufacturer = manufacturer;
        this.seller = seller;
    }

    public static CartItemDto createCartItemDto(Long cartId, Long itemId, Long itemSizeId, String size, String name, int price,
                                                String repImgUrl, int count, int currentPrice, String manufacturer, String seller) {

        return CartItemDto.builder().cartId(cartId).itemId(itemId).itemSizeId(itemSizeId).size(size).name(name)
                .price(price).repImgUrl(repImgUrl).count(count).currentPrice(currentPrice)
                .manufacturer(manufacturer).seller(seller).build();
    }
}
