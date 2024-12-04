package com.example.shop.dto.instagram;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentRequestDto {
    private Long articleId;
    private Long parentCommentId;
    private String content;
}
