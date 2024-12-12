package com.example.shop.dto.item;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ChildrenCategoryRequestDto {
    private Long categoryId;
    private String categoryName;
    private String categoryImageUrl;

    public static ChildrenCategoryRequestDto createDto(Long categoryId, String categoryName,
                                                       String categoryImageUrl) {
        return ChildrenCategoryRequestDto.builder().categoryId(categoryId)
                .categoryName(categoryName).categoryImageUrl(categoryImageUrl).build();
    }
}
