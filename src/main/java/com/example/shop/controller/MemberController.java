package com.example.shop.controller;

import com.example.shop.dto.MemberFormDto;
import com.example.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> saveMember(@RequestBody MemberFormDto dto) {
        memberService.saveMember(dto);
        return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공했습니다.");
    }
}
