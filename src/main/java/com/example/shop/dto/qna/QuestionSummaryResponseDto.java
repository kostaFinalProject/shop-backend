package com.example.shop.dto.qna;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class QuestionSummaryResponseDto {
    private Long questionId;
    private Long memberId;
    private String repImgYn;
    private String itemName;
    private int itemPrice;
    private String title;
    private String isMe;
    private LocalDateTime createAt;
    private String questionStatus;
    private String content;

    public static QuestionSummaryResponseDto readAllDto(Long questionId, Long memberId, String repImgYn,
                                                        String title, LocalDateTime createAt,
                                                        String questionStatus){
        return QuestionSummaryResponseDto.builder().questionId(questionId).memberId(memberId)
                .repImgYn(repImgYn).title(title)
                .createAt(createAt).questionStatus(questionStatus).build();
    }

    public static QuestionSummaryResponseDto readDetailDto(Long questionId, Long memberId, String repImgYn, String itemName, int itemPrice,
                                                           String title, String isMe, LocalDateTime createAt,
                                                           String questionStatus, String content){
        return QuestionSummaryResponseDto.builder().questionId(questionId).memberId(memberId)
                .repImgYn(repImgYn).itemName(itemName).itemPrice(itemPrice).title(title)
                .isMe(isMe).createAt(createAt).questionStatus(questionStatus).content(content).build();
    }
}
