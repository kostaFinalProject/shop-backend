package com.example.shop.dto.instagram.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReplyCommentResponseDto {
    private Long commentId;
    private String memberProfileImageUrl;
    private String memberName;
    private String content;
    private String imageUrl;
    private long likeCount;
    private Long likeId;
    private String isMe;
    private LocalDateTime time;

    public static ReplyCommentResponseDto createDto(Long commentId, String memberProfileImageUrl, String memberName, String content,
                                                    String imageUrl, long likeCount, Long likeId, String isMe, LocalDateTime time) {

        return ReplyCommentResponseDto.builder().commentId(commentId).memberProfileImageUrl(memberProfileImageUrl).memberName(memberName)
                .content(content).imageUrl(imageUrl).likeCount(likeCount).likeId(likeId).isMe(isMe).time(time).build();
    }
}
