package com.example.shop.dto.instagram.comment;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReplyCommentResponseDto {
    private Long commentId;
    private String memberName;
    private String content;
    private String imageUrl;
    private long likeCount;
    private Long likeId;

    public static ReplyCommentResponseDto createDto(Long commentId, String memberName, String content,
                                                    String imageUrl, long likeCount, Long likeId) {

        return ReplyCommentResponseDto.builder().commentId(commentId).memberName(memberName)
                .content(content).imageUrl(imageUrl).likeCount(likeCount).likeId(likeId).build();
    }
}
