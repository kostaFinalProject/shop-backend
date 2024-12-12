package com.example.shop.dto.item;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DiscountResponseDto {
    private Long discountId;
    private int discountPrice;

    public static DiscountResponseDto createDto(Long discountId, int discountPrice) {
        return DiscountResponseDto.builder().discountId(discountId).discountPrice(discountPrice).build();
    }
}
