package com.example.shop.dto.instagram.article;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ArticleSummaryResponseDto {
    private Long articleId;
    private Long memberId;
    private String memberName;
    private String imageUrl;
    private String content;
    private long likeCount;
    private long viewCount;
    private boolean liked;
    private Long likeId;

    public static ArticleSummaryResponseDto createDto(Long articleId, Long memberId, String memberName,
                                                      String imageUrl, String content, long likeCount,
                                                      long viewCount, boolean liked, Long likeId) {

        return ArticleSummaryResponseDto.builder().articleId(articleId).memberId(memberId).memberName(memberName)
                .imageUrl(imageUrl).content(content).likeCount(likeCount)
                .viewCount(viewCount).liked(liked).likeId(likeId).build();
    }
}
