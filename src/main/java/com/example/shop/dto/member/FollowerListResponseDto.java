package com.example.shop.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class FollowerListResponseDto {
    private Long followId;
    private Long memberId;
    private String memberName;

    public static FollowerListResponseDto createDto(Long followId, Long memberId, String memberName) {
        return FollowerListResponseDto.builder().followId(followId).memberId(memberId).memberName(memberName).build();
    }
}
