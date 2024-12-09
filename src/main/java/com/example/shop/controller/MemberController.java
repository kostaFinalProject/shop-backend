package com.example.shop.controller;

import com.example.shop.dto.member.MemberProfileUpdateDto;
import com.example.shop.dto.member.MemberSignUpDto;
import com.example.shop.dto.member.MemberUpdateDto;
import com.example.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> saveMember(@RequestBody MemberSignUpDto dto) {
        memberService.saveMember(dto);
        return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공했습니다.");
    }

    /** 회원별 게시글 조회 */
    @GetMapping("/{memberId}/{targetId}/articles")
    public ResponseEntity<?> getMemberArticles(@PathVariable("memberId") Long memberId,
                                               @PathVariable("targetId") Long targetId,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "15") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getArticle(memberId, targetId, pageable));
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

    /** 회원의 팔로워 리스트 조회 */
    @GetMapping("/{memberId}/followees")
    public ResponseEntity<?> getMemberFolloweeList(@PathVariable("memberId") Long memberId,
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "30") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getFollowee(memberId, pageable));
    }

    /** 팔로우를 신청받은 회원의 팔로우 요청 리스트 조회 */
    @GetMapping("/{memberId}/requests")
    public ResponseEntity<?> getMemberFolloweeRequestList(@PathVariable("memberId") Long memberId,
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "30") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getFolloweeRequest(memberId, pageable));
    }

    /** 회원의 차단 리스트 조회 */
    @GetMapping("/{memberId}/blocks")
    public ResponseEntity<?> getMemberBlockList(@PathVariable("memberId") Long memberId,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "30") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getBlockList(memberId, pageable));
    }

    /** 회원 정보 수정 */
    @PutMapping("{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable("memberId") Long memberId,
                                          @RequestBody MemberUpdateDto dto) {
        memberService.updateMemberInfo(memberId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("회원수정에 성공했습니다.");
    }

    /** 회원 프로필 상태 수정 */
    @PutMapping("/{memberId}/accountstatus")
    public ResponseEntity<?> updateMemberAccountStatus(@PathVariable("memberId") Long memberId) {
        memberService.updateMemberAccountStatus(memberId);
        return ResponseEntity.status(HttpStatus.OK).body("회원 공개 여부를 수정했습니다.");
    }

    /** 회원 프로필 수정 */
    @PutMapping("/{memberId}/profile")
    public ResponseEntity<?> updateMemberProfile(@PathVariable("memberId") Long memberId,
                                                 @RequestPart("profile") MemberProfileUpdateDto dto,
                                                 @RequestPart(value = "file", required = false) MultipartFile file) {

        memberService.updateMemberProfileImg(memberId, dto, file);
        return ResponseEntity.status(HttpStatus.OK).body("회원 프로필을 수정했습니다.");
    }

    /** 관리자 신청 승인 */
    @PutMapping("/{memberId}/{targetId}/promotion")
    public ResponseEntity<?> promotionAdmin(@PathVariable("memberId") Long memberId,
                                            @PathVariable("targetId") Long targetId) {
        memberService.promotionAdmin(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("관리자로 승인했습니다.");
    }

    /** 관리자 권한 해제 */
    @PutMapping("/{memberId}/{targetId}/relegation")
    public ResponseEntity<?> relegationAdmin(@PathVariable("memberId") Long memberId,
                                             @PathVariable("targetId") Long targetId) {
        memberService.relegationAdmin(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("관리자 권한을 해제했습니다.");
    }

    /** 게시글 권한 해제 */
    @PutMapping("/{memberId}/{targetId}/suspension")
    public ResponseEntity<?> suspendedArticle(@PathVariable("memberId") Long memberId,
                                              @PathVariable("targetId") Long targetId) {
        memberService.suspendedArticle(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("게시판 이용 권한을 중지했습니다.");
    }

    /** 게시글 권한 승인 */
    @PutMapping("/{memberId}/{targetId}/restart")
    public ResponseEntity<?> restartArticle(@PathVariable("memberId") Long memberId,
                                            @PathVariable("targetId") Long targetId) {
        memberService.restartArticle(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("게시판 이용 권한을 재부여했습니다.");
    }
}