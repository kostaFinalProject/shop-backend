package com.example.shop.dto.qna;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class QuestionRequestDto {
    private Long itemId;
    private String title;
    private String content;
}