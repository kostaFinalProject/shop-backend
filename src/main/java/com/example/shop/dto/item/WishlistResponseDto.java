package com.example.shop.dto.item;

public class WishlistResponseDto {
    private Long itemId;
    private String itemName;
    private int price;

    public WishlistResponseDto(Long itemId, String itemName, int price) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
    }
}