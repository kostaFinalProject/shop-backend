package com.example.shop.controller;

import com.example.shop.dto.MemberFormDto;
import com.example.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Spring Security 적용하면 @PathVariable("memberId") 제거
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    /** 회원 가입 */
    @PostMapping
    public ResponseEntity<?> saveMember(@RequestBody MemberFormDto dto) {
        memberService.saveMember(dto);
        return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공했습니다.");
    }

    /** 회원별 게시글 조회 */
    @GetMapping("/{memberId}/{fromMemberId}/articles")
    public ResponseEntity<?> getMemberArticles(@PathVariable("memberId") Long memberId,
                                               @PathVariable("fromMemberId") Long fromMemberId,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "15") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getArticle(memberId, fromMemberId, pageable));
    }

    /** 회원별 저장된 게시글 조회 */
    @GetMapping("/{memberId}/articlecollections")
    public ResponseEntity<?> getMemberArticleCollections(@PathVariable("memberId") Long memberId,
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getArticleCollection(memberId, pageable));
    }

    /** 회원의 팔로우 리스트 조회 */
    @GetMapping("/{memberId}/followers")
    public ResponseEntity<?> getMemberFollowerList(@PathVariable("memberId") Long memberId,
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "30") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getFollower(memberId, pageable));
    }

    /** 회원의 차단 리스트 조회 */
    @GetMapping("/{memberId}/blocks")
    public ResponseEntity<?> getMemberBlockList(@PathVariable("memberId") Long memberId,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "30") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getBlockList(memberId, pageable));
    }
}