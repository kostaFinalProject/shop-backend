package com.example.shop.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ArticleItemResponseDto {
    private Long itemId;
    private String itemName;
    private String imageUrl;

    public static ArticleItemResponseDto createDto(Long itemId, String itemName, String imageUrl) {
        return ArticleItemResponseDto.builder().itemId(itemId).itemName(itemName).imageUrl(imageUrl).build();
    }
}
