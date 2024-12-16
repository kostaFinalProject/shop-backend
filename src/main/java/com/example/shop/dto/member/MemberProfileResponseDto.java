package com.example.shop.dto.member;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberProfileResponseDto {
    private Long memberId;
    private String memberNickname;
    private String introduction;
    private String memberProfileImageUrl;
    private String memberStatus;
    private Long followId;
    private int articleCount;
    private long followeeCount;
    private long followerCount;
    private String follow;

    public static MemberProfileResponseDto createDto(Long memberId, String memberNickname, String introduction,
                                                     String memberProfileImageUrl, String memberStatus,
                                                     int articleCount, long followeeCount, long followerCount,
                                                     Long followId, String follow) {

        return MemberProfileResponseDto.builder().memberId(memberId).memberNickname(memberNickname)
                .introduction(introduction).memberProfileImageUrl(memberProfileImageUrl).memberStatus(memberStatus)
                .articleCount(articleCount).followeeCount(followeeCount).followerCount(followerCount)
                .followId(followId).follow(follow).build();
    }
}
