package com.example.shop.dto.qna;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AnswerResponseDto {
    private Long answerId;
    private Long questionId;

    private String userId;
    private String createAt;
    private String content;

    public static AnswerResponseDto readAnswerDto(Long answerId, Long questionId, String userId,
                                                  String createAt, String content){
        return AnswerResponseDto.builder().answerId(answerId).questionId(questionId).userId(userId)
                .createAt(createAt).content(content).build();
    }
}
