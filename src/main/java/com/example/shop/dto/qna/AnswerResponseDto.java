package com.example.shop.dto.qna;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AnswerResponseDto {
    private Long answerId;
    private Long questionId;
    private Long memberId;
    private LocalDateTime createAt;
    private String content;

    public static AnswerResponseDto readAnswerDto(Long answerId, Long questionId, Long memberId,
                                                  LocalDateTime createAt, String content){
        return AnswerResponseDto.builder().answerId(answerId).questionId(questionId).memberId(memberId)
                .createAt(createAt).content(content).build();
    }
}
