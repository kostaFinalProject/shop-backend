package com.example.shop.dto.item;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ItemDetailResponseDto {
    private Long itemId;
    private String itemCategory;
    private String manufacturer;
    private String name;
    private int price;
    private String seller;
    private List<ItemSizeResponseDto> itemSizes;
    private List<String> imageUrls;

    public static ItemDetailResponseDto createDto(Long itemId, String itemCategory, String manufacturer, String name,
                                                  int price, String seller, List<ItemSizeResponseDto> itemSizes, List<String> imageUrls) {

        return ItemDetailResponseDto.builder().itemId(itemId).itemCategory(itemCategory).manufacturer(manufacturer).name(name)
                .price(price).seller(seller).itemSizes(itemSizes).imageUrls(imageUrls).build();
    }
}
