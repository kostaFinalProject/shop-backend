package com.example.shop.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberListResponseDto {
    private Long memberId;
    private String memberNickname;
    private String memberProfileImageUrl;

    public static MemberListResponseDto createDto(Long memberId, String memberNickname,
                                                  String memberProfileImageUrl) {

        return MemberListResponseDto.builder().memberId(memberId).memberNickname(memberNickname)
                .memberProfileImageUrl(memberProfileImageUrl).build();
    }
}
