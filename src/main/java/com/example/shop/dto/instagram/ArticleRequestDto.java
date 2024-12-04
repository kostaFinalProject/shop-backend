package com.example.shop.dto.instagram;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ArticleRequestDto {
    String content;
    List<String> hashtags = new ArrayList<>();
    List<Long> itemIds = new ArrayList<>();
}
