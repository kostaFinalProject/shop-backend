package com.example.shop.dto.instagram.article;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ArticleDetailResponseDto {
    private Long memberId;
    private String memberName;
    private List<String> images;
    private List<String> hashtags;
    private List<ArticleItemResponseDto> items;
}
