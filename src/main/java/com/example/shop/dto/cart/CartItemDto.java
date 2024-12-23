package com.example.shop.dto.cart;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemDto {
    private Long cartId;
    private Long itemSizeId;
    private String name;
    private int price;
    private String repImgUrl;
    private int count;
    private int currentPrice;

    @Builder
    private CartItemDto(Long cartId, Long itemSizeId, String name, int price,
                        String repImgUrl, int count, int currentPrice) {
        this.cartId = cartId;
        this.itemSizeId = itemSizeId;
        this.name = name;
        this.price = price;
        this.repImgUrl = repImgUrl;
        this.count = count;
        this.currentPrice = currentPrice;
    }

    public static CartItemDto createCartItemDto(Long cartId, Long itemSizeId, String name,
                                                int price, String repImgUrl, int count, int currentPrice) {

        return CartItemDto.builder().cartId(cartId).itemSizeId(itemSizeId).name(name)
                .price(price).repImgUrl(repImgUrl).count(count).currentPrice(currentPrice).build();
    }
}
