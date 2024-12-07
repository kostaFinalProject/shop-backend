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
    private Long likeId;

    public static CommentResponseDto createDto(Long commentId, String memberName, String content,
                                               String imageUrl, long likeCount, boolean hasReplies, Long likeId) {

        return CommentResponseDto.builder().commentId(commentId).memberName(memberName).content(content)
                .imageUrl(imageUrl).likeCount(likeCount).hasReplies(hasReplies).likeId(likeId).build();
    }
}
