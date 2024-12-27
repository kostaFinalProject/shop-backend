package com.example.shop.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberProfileSummaryResponseDto {
    private Long memberId;
    private String memberNickname;
    private String memberIntroduction;
    private String memberProfileImageUrl;
    private String memberStatus;

    public static MemberProfileSummaryResponseDto createDto(Long memberId, String memberNickname,
                                                            String memberIntroduction, String memberProfileImageUrl,
                                                            String memberStatus) {

        return MemberProfileSummaryResponseDto.builder().memberId(memberId).memberNickname(memberNickname)
                .memberIntroduction(memberIntroduction).memberProfileImageUrl(memberProfileImageUrl)
                .memberStatus(memberStatus).build();
    }
}
