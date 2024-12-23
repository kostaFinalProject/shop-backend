package com.example.shop.dto.member;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberResponseDto {
    private Long memberId;
    private String name;
    private String nickname;
    private String email;
    private String phone;
    private String postCode;
    private String roadAddress;
    private String detailAddress;
    private String grade;
    private String pointGrade;
    private int point;
    private int payment;

    public static MemberResponseDto createDto(Long memberId, String name, String nickname,
                                              String email, String phone, String postCode,
                                              String roadAddress, String detailAddress, String grade,
                                              String pointGrade, int point, int payment) {

        return MemberResponseDto.builder().memberId(memberId).name(name).nickname(nickname)
                .email(email).phone(phone).postCode(postCode).roadAddress(roadAddress)
                .detailAddress(detailAddress).grade(grade)
                .pointGrade(pointGrade).point(point).payment(payment).build();
    }
}
