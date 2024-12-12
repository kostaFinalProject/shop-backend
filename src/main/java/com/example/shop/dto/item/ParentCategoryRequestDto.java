package com.example.shop.dto.item;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ParentCategoryRequestDto {
    private Long parentCategoryId;
    private String name;

    public static ParentCategoryRequestDto createDto(Long parentCategoryId, String name) {
        return ParentCategoryRequestDto.builder().parentCategoryId(parentCategoryId).name(name).build();
    }
}
