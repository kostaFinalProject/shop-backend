package com.example.shop.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AdminRequestMemberDto {
    private Long memberId;
    private String userId;
    private String grade;

    public static AdminRequestMemberDto createDto(Long memberId, String userId, String grade) {
        return AdminRequestMemberDto.builder().memberId(memberId).userId(userId).grade(grade).build();
    }
}
