package com.example.shop.dto.instagram.article;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ArticleSummaryResponseDto {
    private Long articleId;
    private Long memberId;
    private String memberName;
    private String memberProfileImageUrl;
    private String imageUrl;
    private String content;
    private long likeCount;
    private long viewCount;
    private Long likeId;
    private List<String> hashtags;

    public static ArticleSummaryResponseDto createDto(Long articleId, Long memberId, String memberName,
                                                      String memberProfileImageUrl, String imageUrl,
                                                      String content, long likeCount,
                                                      long viewCount, Long likeId, List<String> hashtags) {

        return ArticleSummaryResponseDto.builder().articleId(articleId).memberId(memberId).memberName(memberName)
                .memberProfileImageUrl(memberProfileImageUrl).imageUrl(imageUrl).content(content).likeCount(likeCount)
                .viewCount(viewCount).likeId(likeId).hashtags(hashtags).build();
    }
}
