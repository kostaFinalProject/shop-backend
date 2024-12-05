package com.example.shop.dto.instagram.comment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReplyCommentResponseDto {
    private Long commentId;
    private String memberName;
    private String content;
    private String imageUrl;
    private long likeCount;
}
