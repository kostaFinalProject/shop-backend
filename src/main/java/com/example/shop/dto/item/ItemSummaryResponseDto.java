package com.example.shop.dto.item;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ItemSummaryResponseDto {
    private Long itemId;
    private String itemCategory;
    private String manufacturer;
    private String name;
    private int price;
    private String repImgUrl;
    private String itemStatus;
    private String seller;
    private int discountPercent;
    private int discountPrice;

    public static ItemSummaryResponseDto createDto(Long itemId, String itemCategory, String manufacturer,
                                            String name, int price, String repImgUrl,
                                                   String itemStatus, String seller,
                                                   int discountPercent, int discountPrice) {

        return ItemSummaryResponseDto.builder().itemId(itemId).itemCategory(itemCategory).manufacturer(manufacturer)
                .name(name).price(price).repImgUrl(repImgUrl).itemStatus(itemStatus).seller(seller)
                .discountPercent(discountPercent).discountPrice(discountPrice).build();
    }
}
