package com.example.shop.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BlockResponseDto {
    private Long blockId;
    private Long blockMemberId;
    private String blockMemberName;

    public static BlockResponseDto createDto(Long blockId, Long blockMemberId, String blockMemberName) {
        return BlockResponseDto.builder().blockId(blockId).blockMemberId(blockMemberId)
                .blockMemberName(blockMemberName).build();
    }
}
