package com.example.shop.dto.item;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemRequestDto {
    private String itemCategory;
    private String manufacturer;
    private String name;
    private int price;
    private List<ItemSizeDto> itemSizes = new ArrayList<>();
}
