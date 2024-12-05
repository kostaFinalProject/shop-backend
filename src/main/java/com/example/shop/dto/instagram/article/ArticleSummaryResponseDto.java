package com.example.shop.dto.instagram.article;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ArticleSummaryResponseDto {
    private Long memberId;
    private String memberName;
    private String imageUrl;
    private String content;
    private long likeCount;
}
