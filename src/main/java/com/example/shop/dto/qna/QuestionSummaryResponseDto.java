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
    private Long itemId;
    private String repImgYn;
    private String itemName;
    private int itemPrice;
    private String title;
    private String memberNickname;
    private String isMe;
    private LocalDateTime createAt;
    private String questionStatus;
    private String content;
    private String memberGrade;

    public static QuestionSummaryResponseDto readAllDto(Long questionId, Long memberId, Long itemId, String repImgYn, String memberNickname,
                                                        String title, LocalDateTime createAt,
                                                        String questionStatus){
        return QuestionSummaryResponseDto.builder().questionId(questionId).memberId(memberId).itemId(itemId)
                .repImgYn(repImgYn).memberNickname(memberNickname).title(title)
                .createAt(createAt).questionStatus(questionStatus).build();
    }

    public static QuestionSummaryResponseDto readDetailDto(Long questionId, Long memberId, Long itemId, String repImgYn, String itemName, int itemPrice,
                                                           String title, String memberNickname, String isMe, LocalDateTime createAt,
                                                           String questionStatus, String content, String memberGrade){
        return QuestionSummaryResponseDto.builder().questionId(questionId).memberId(memberId).itemId(itemId)
                .repImgYn(repImgYn).itemName(itemName).itemPrice(itemPrice).title(title).memberNickname(memberNickname)
                .isMe(isMe).createAt(createAt).questionStatus(questionStatus).content(content).memberGrade(memberGrade).build();
    }
}
