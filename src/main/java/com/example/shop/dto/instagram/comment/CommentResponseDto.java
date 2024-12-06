package com.example.shop.dto.instagram.comment;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CommentResponseDto {
    private Long commentId;
    private String memberName;
    private String content;
    private String imageUrl;
    private long likeCount;
    private boolean hasReplies;
    private boolean liked;
    private Long likeId;

    public static CommentResponseDto createDto(Long commentId, String memberName, String content,
                                               String imageUrl, long likeCount, boolean hasReplies,
                                               boolean liked, Long likeId) {

        return CommentResponseDto.builder().commentId(commentId).memberName(memberName).content(content)
                .imageUrl(imageUrl).likeCount(likeCount).hasReplies(hasReplies)
                .liked(liked).likeId(likeId).build();
    }
}
