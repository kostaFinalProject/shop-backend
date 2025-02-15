package com.example.shop.dto.instagram.article;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ArticleItemResponseDto {
    private Long itemId;
    private String itemName;
    private int price;
    private String imageUrl;
    private String itemStatus;

    public static ArticleItemResponseDto createDto(Long itemId, String itemName, int price,
                                                   String imageUrl, String itemStatus) {
        return ArticleItemResponseDto.builder().itemId(itemId).itemName(itemName)
                .price(price).imageUrl(imageUrl).itemStatus(itemStatus).build();
    }
}
