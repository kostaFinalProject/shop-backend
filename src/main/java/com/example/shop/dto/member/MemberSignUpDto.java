package com.example.shop.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberSignUpDto {
    private String userId;
    private String password;
    private String name;
    private String nickname;
    private String email;
    private String phone;
    private String postCode;
    private String roadAddress;
    private String detailAddress;
    private String grade;
    private String provider;
}