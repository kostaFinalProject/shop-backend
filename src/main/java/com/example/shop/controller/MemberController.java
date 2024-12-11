package com.example.shop.controller;

import com.example.shop.aop.PublicApi;
import com.example.shop.aop.SecurityAspect;
import com.example.shop.aop.TokenApi;
import com.example.shop.dto.login.LoginDto;
import com.example.shop.dto.login.LoginResponseDto;
import com.example.shop.dto.login.RefreshTokenResponse;
import com.example.shop.dto.member.*;
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
    @PublicApi
    @PostMapping
    public ResponseEntity<?> saveMember(@RequestBody MemberSignUpDto dto) {
        memberService.saveMember(dto);
        return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공했습니다.");
    }

    /** 회원 정보 조회 */
    @GetMapping
    public ResponseEntity<?> getMember() {
        Long memberId = SecurityAspect.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberInfo(memberId));
    }

    /** 회원별 게시글 조회 */
    @GetMapping("/articles/{targetId}")
    public ResponseEntity<?> getMemberArticles(@PathVariable("targetId") Long targetId,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "15") int size) {

        Long memberId = SecurityAspect.getCurrentMemberId();

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getArticle(memberId, targetId, pageable));
    }

    /** 회원별 저장된 게시글 조회 */
    @GetMapping("/articlecollections")
    public ResponseEntity<?> getMemberArticleCollections(@RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "15") int size) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getArticleCollection(memberId, pageable));
    }

    /** 회원의 팔로우 리스트 조회 */
    @GetMapping("/followers/{targetMemberId}")
    public ResponseEntity<?> getMemberFollowerList(@PathVariable("targetMemberId") Long targetMemberId,
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "30") int size) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getFollower(targetMemberId, memberId, pageable));
    }

    /** 회원의 팔로워 리스트 조회 */
    @GetMapping("/followees/{fromMemberId}/{memberId}")
    public ResponseEntity<?> getMemberFolloweeList(@PathVariable("memberId") Long memberId,
                                                   @PathVariable("fromMemberId") Long fromMemberId,
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "30") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getFollowee(memberId, fromMemberId, pageable));
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

    /** 전체 회원 조회 */
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getMembers(@PathVariable("memberId") Long memberId,
                                        @RequestParam(value = "nickname") String nickname,
                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "30") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberList(nickname, memberId, pageable));
    }

    /** 회원 정보 수정 */
    @PutMapping("{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable("memberId") Long memberId,
                                          @RequestBody MemberUpdateDto dto) {
        memberService.updateMemberInfo(memberId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("회원수정에 성공했습니다.");
    }

    /** 회원 공개 여부 상태 수정 */
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

    /** 로그인 */
    @PublicApi
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        String tokens = memberService.login(loginDto);
        String[] splitTokens = tokens.split(":");
        return ResponseEntity.status(HttpStatus.OK).body(LoginResponseDto.createLoginResponseDto(
                "Bearer " + splitTokens[0], "Bearer " + splitTokens[1]));
    }

    /** 로그아웃 */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token, @RequestHeader("Refresh-Token") String refreshToken) {
        String jwtToken = token.replace("Bearer ", "");
        String jwtRefreshToken = refreshToken.replace("Bearer ", "");
        memberService.logout(jwtToken, jwtRefreshToken);
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃에 성공했습니다.");
    }

    /** Access Token 재발급 */
    @TokenApi
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("Refresh-Token") String refreshToken) {
        String jwtRefreshToken = refreshToken.replace("Bearer ", "");
        String newAccessToken = memberService.refreshAccessToken(jwtRefreshToken);
        return ResponseEntity.status(HttpStatus.OK)
                .body(RefreshTokenResponse.createDto("Bearer " + newAccessToken));
    }

    /** 관리자 신청 승인 */
    @PutMapping("/promotion/{memberId}/{targetId}")
    public ResponseEntity<?> promotionAdmin(@PathVariable("memberId") Long memberId,
                                            @PathVariable("targetId") Long targetId) {
        memberService.promotionAdmin(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("관리자로 승인했습니다.");
    }

    /** 관리자 권한 해제 */
    @PutMapping("/relegation/{memberId}/{targetId}")
    public ResponseEntity<?> relegationAdmin(@PathVariable("memberId") Long memberId,
                                             @PathVariable("targetId") Long targetId) {
        memberService.relegationAdmin(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("관리자 권한을 해제했습니다.");
    }

    /** 게시글 권한 해제 */
    @PutMapping("/suspension/{memberId}/{targetId}")
    public ResponseEntity<?> suspendedArticle(@PathVariable("memberId") Long memberId,
                                              @PathVariable("targetId") Long targetId) {
        memberService.suspendedArticle(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("게시판 이용 권한을 중지했습니다.");
    }

    /** 게시글 권한 재승인 */
    @PutMapping("/enable/{memberId}/{targetId}")
    public ResponseEntity<?> enableArticle(@PathVariable("memberId") Long memberId,
                                           @PathVariable("targetId") Long targetId) {
        memberService.enableArticle(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("게시판 이용 권한을 재부여했습니다.");
    }
}