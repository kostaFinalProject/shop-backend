package com.example.shop.dto.item;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ItemDetailResponseDto {
    private Long itemId;
    private Long discountId;
    private String parentCategory;
    private String itemCategory;
    private String manufacturer;
    private String name;
    private int price;
    private String seller;
    private List<ItemSizeResponseDto> itemSizes;
    private List<String> imageUrls;
    private String itemDetailImageUrl;
    private int discountPercent;
    private int discountPrice;
    private String memberGrade;

    public static ItemDetailResponseDto createDto(Long itemId, Long discountId, String parentCategory, String itemCategory, String manufacturer, String name,
                                                  int price, String seller, List<ItemSizeResponseDto> itemSizes, List<String> imageUrls,
                                                  String itemDetailImageUrl, int discountPercent, int discountPrice, String memberGrade) {

        return ItemDetailResponseDto.builder().itemId(itemId).discountId(discountId).parentCategory(parentCategory)
                .itemCategory(itemCategory).manufacturer(manufacturer).name(name).price(price).seller(seller)
                .itemSizes(itemSizes).imageUrls(imageUrls).itemDetailImageUrl(itemDetailImageUrl)
                .discountPercent(discountPercent).discountPrice(discountPrice).memberGrade(memberGrade).build();
    }
}
