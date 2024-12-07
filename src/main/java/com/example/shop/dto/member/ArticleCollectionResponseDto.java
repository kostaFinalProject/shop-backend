package com.example.shop.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ArticleCollectionResponseDto {
    private Long articleCollectionId;
    private Long articleId;
    private Long memberId;
    private String memberName;
    private String imageUrl;
    private String content;
    private long likeCount;
    private long viewCount;
    private Long likeId;

    public static ArticleCollectionResponseDto createDto(Long articleCollectionId, Long articleId, Long memberId,
                                                         String memberName, String imageUrl, String content,
                                                         long likeCount, long viewCount, Long likeId) {

        return ArticleCollectionResponseDto.builder().articleCollectionId(articleCollectionId).articleId(articleId).memberId(memberId)
                .memberName(memberName).imageUrl(imageUrl).content(content)
                .likeCount(likeCount).viewCount(viewCount).likeId(likeId).build();
    }
}
