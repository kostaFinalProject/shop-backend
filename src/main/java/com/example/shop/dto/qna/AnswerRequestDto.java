package com.example.shop.dto.qna;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerRequestDto {

    private Long questionId;
    private String content;
}
