package com.example.shop.dto.qna;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class QuestionSummaryResponseDto {
    private Long questionId;
    private String repImgYn;
    private String itemName;
    private int itemPrice;
    private String title;
    private String userId;
    private String isMe;
    private String createAt;
    private String questionStatus;
    private String content;

    public static QuestionSummaryResponseDto readAllDto(Long questionId, String repImgYn,
                                                        String title, String userId, String createAt,
                                                        String questionStatus){
        return QuestionSummaryResponseDto.builder().questionId(questionId).repImgYn(repImgYn).title(title).userId(userId)
                .createAt(createAt).questionStatus(questionStatus).build();
    }

    public static QuestionSummaryResponseDto readDetailDto(Long questionId, String repImgYn, String itemName, int itemPrice,
                                                           String title, String userId, String isMe, String createAt,
                                                           String questionStatus, String content){
        return QuestionSummaryResponseDto.builder().questionId(questionId).repImgYn(repImgYn).itemName(itemName).itemPrice(itemPrice).title(title).userId(userId)
                .isMe(isMe).createAt(createAt).questionStatus(questionStatus).content(content).build();
    }
}
