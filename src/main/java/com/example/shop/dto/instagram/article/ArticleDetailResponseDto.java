package com.example.shop.dto.instagram.article;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ArticleDetailResponseDto {
    private Long articleId;
    private Long memberId;
    private String memberName;
    private String memberProfileImageUrl;
    private List<String> images;
    private List<String> hashtags;
    private List<ArticleItemResponseDto> articleItems;
    private String content;
    private String isFollowing;
    private long likeCount;
    private long commentCount;
    private Long likeId;
    private LocalDateTime createdAt;

    public static ArticleDetailResponseDto createDto(Long articleId, Long memberId, String memberName, String memberProfileImageUrl,
                                                     List<String> images, List<String> hashtags, List<ArticleItemResponseDto> articleItems,
                                                     String content, String isFollowing, long likeCount, long commentCount,
                                                     Long likeId, LocalDateTime createdAt) {

        return ArticleDetailResponseDto.builder().articleId(articleId).memberId(memberId).memberName(memberName)
                .memberProfileImageUrl(memberProfileImageUrl).images(images).hashtags(hashtags).articleItems(articleItems).content(content)
                .isFollowing(isFollowing).likeCount(likeCount).commentCount(commentCount).likeId(likeId).createdAt(createdAt).build();
    }
}
