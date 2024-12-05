package com.example.shop.dto.instagram.article;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ArticleItemResponseDto {
    private Long itemId;
    private String itemName;
    private int price;
    private String imageUrl;
}
