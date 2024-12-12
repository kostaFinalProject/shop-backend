package com.example.shop.dto.item;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ItemSizeResponseDto {
    private Long itemSizeId;
    private String itemSize;
    private int stockQuantity;

    public static ItemSizeResponseDto createDto(Long itemSizeId, String itemSize, int stockQuantity) {
        return ItemSizeResponseDto.builder().itemSizeId(itemSizeId).itemSize(itemSize)
                .stockQuantity(stockQuantity).build();
    }
}
