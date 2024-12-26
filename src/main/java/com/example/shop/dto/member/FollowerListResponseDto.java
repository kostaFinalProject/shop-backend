package com.example.shop.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FollowerListResponseDto {
    private Long followId;
    private Long memberId;
    private String memberProfileImageUrl;
    private String memberNickname;
    private String introduction;
    private String followStatus;

    public static FollowerListResponseDto createDto(Long followId, Long memberId, String memberProfileImageUrl,
                                                    String memberNickname, String introduction, String followStatus) {
        return FollowerListResponseDto.builder().followId(followId).memberId(memberId)
                .memberProfileImageUrl(memberProfileImageUrl).memberNickname(memberNickname)
                .introduction(introduction).followStatus(followStatus).build();
    }
}
