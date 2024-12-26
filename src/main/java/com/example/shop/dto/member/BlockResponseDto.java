package com.example.shop.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BlockResponseDto {
    private Long blockId;
    private Long blockMemberId;
    private String memberProfileImageUrl;
    private String blockMemberNickname;
    private String blockMemberIntroduction;

    public static BlockResponseDto createDto(Long blockId, Long blockMemberId, String memberProfileImageUrl,
                                             String blockMemberNickname, String blockMemberIntroduction) {
        return BlockResponseDto.builder().blockId(blockId).blockMemberId(blockMemberId)
                .memberProfileImageUrl(memberProfileImageUrl).blockMemberNickname(blockMemberNickname)
                .blockMemberIntroduction(blockMemberIntroduction).build();
    }
}
