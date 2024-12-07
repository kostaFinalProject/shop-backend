package com.example.shop.dto.instagram.article;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ArticleDetailResponseDto {
    private Long articleId;
    private Long memberId;
    private String memberName;
    private List<String> images;
    private List<String> hashtags;
    private List<ArticleItemResponseDto> items;
    private long likeCount;
    private long commentCount;
    private Long likeId;

    public static ArticleDetailResponseDto createDto(Long articleId, Long memberId, String memberName,
                                                     List<String> images, List<String> hashtags, List<ArticleItemResponseDto> items,
                                                     long likeCount, long commentCount, Long likeId) {

        return ArticleDetailResponseDto.builder().articleId(articleId).memberId(memberId).memberName(memberName)
                .images(images).hashtags(hashtags).items(items)
                .likeCount(likeCount).commentCount(commentCount).likeId(likeId).build();
    }
}
