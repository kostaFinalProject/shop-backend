package com.example.shop.controller;

import com.example.shop.aop.PublicApi;
import com.example.shop.aop.SecurityAspect;
import com.example.shop.aop.TokenApi;
import com.example.shop.config.CustomUserDetails;
import com.example.shop.dto.login.LoginDto;
import com.example.shop.dto.login.LoginResponseDto;
import com.example.shop.dto.login.OAuthLoginDto;
import com.example.shop.dto.login.RefreshTokenResponse;
import com.example.shop.dto.member.*;
import com.example.shop.service.MemberService;
import com.example.shop.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final ValidationService validationService;

    /** 회원 가입 */
    @PublicApi
    @PostMapping
    public ResponseEntity<?> saveMember(@RequestBody MemberSignUpDto dto) {
        memberService.saveMember(dto);
        return ResponseEntity.status(HttpStatus.OK).body("회원가입에 성공했습니다.");
    }

    /** 관리자 권한 신청자 조회 */
    @GetMapping("/admin-request")
    public ResponseEntity<?> getAdminRequest(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "10") int size) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getAdminRequestMember(pageable));
    }

    /** 회원 정보 조회 */
    @GetMapping
    public ResponseEntity<?> getMember() {
        Long memberId = SecurityAspect.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberInfo(memberId));
    }

    /** 회원별 게시글 조회 */
    @PublicApi
    @GetMapping("/articles/{targetId}")
    public ResponseEntity<?> getMemberArticles(@PathVariable("targetId") Long targetId,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "15") int size) {

        Long memberId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            if (userDetails.getUsername() != null) {
                String userId = userDetails.getUsername();
                memberId = validationService.validateMemberByUserId(userId).getId();
            }
        }
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getArticle(targetId, memberId, pageable));
    }

    /** 회원별 저장된 게시글 조회 */
    @GetMapping("/article-collections")
    public ResponseEntity<?> getMemberArticleCollections(@RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "15") int size) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getArticleCollection(memberId, pageable));
    }

    /** 회원의 팔로우 리스트 조회 */
    @GetMapping("/followers/{targetId}")
    public ResponseEntity<?> getMemberFollowerList(@PathVariable("targetId") Long targetId,
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "30") int size) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getFollower(targetId, memberId, pageable));
    }

    /** 회원의 팔로워 리스트 조회 */
    @GetMapping("/followees/{targetId}")
    public ResponseEntity<?> getMemberFolloweeList(@PathVariable("targetId") Long targetId,
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "30") int size) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getFollowee(targetId, memberId, pageable));
    }

    /** 팔로우를 신청받은 회원의 팔로우 요청 리스트 조회 */
    @GetMapping("/requests")
    public ResponseEntity<?> getMemberFolloweeRequestList(@RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "30") int size) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getFolloweeRequest(memberId, pageable));
    }

    /** 회원의 차단 리스트 조회 */
    @GetMapping("/blocks")
    public ResponseEntity<?> getMemberBlockList(@RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "30") int size) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getBlockList(memberId, pageable));
    }

    /** 차단자를 제외한 전체 회원 조회 */
    @PublicApi
    @GetMapping("/all")
    public ResponseEntity<?> getMembers(@RequestParam(value = "nickname", required = false) String nickname,
                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "30") int size) {

        Long memberId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            if (userDetails.getUsername() != null) {
                String userId = userDetails.getUsername();
                memberId = validationService.validateMemberByUserId(userId).getId();
            }
        }
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberList(nickname, memberId, pageable));
    }

    /** 회원 프로필 조회 */
    @PublicApi
    @GetMapping("/profile/{targetId}")
    public ResponseEntity<?> getMemberProfile(@PathVariable("targetId") Long targetId) {
        Long memberId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            if (userDetails.getUsername() != null) {
                String userId = userDetails.getUsername();
                memberId = validationService.validateMemberByUserId(userId).getId();
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberProfile(targetId, memberId));
    }

    /** 회원 태그 상품 조회 */
    @PublicApi
    @GetMapping("/items/{targetId}")
    public ResponseEntity<?> getMemberItem(@PathVariable("targetId") Long targetId,
                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "30") int size) {
        Long memberId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            if (userDetails.getUsername() != null) {
                String userId = userDetails.getUsername();
                memberId = validationService.validateMemberByUserId(userId).getId();
            }
        }

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(memberService.getArticleItems(targetId, memberId, pageable));
    }

    /** 회원 정보 수정 */
    @PutMapping
    public ResponseEntity<?> updateMember(@RequestBody MemberUpdateDto dto) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        memberService.updateMemberInfo(memberId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("회원수정에 성공했습니다.");
    }

    /** 회원 공개 여부 상태 수정 */
    @PutMapping("/account-status")
    public ResponseEntity<?> updateMemberAccountStatus() {
        Long memberId = SecurityAspect.getCurrentMemberId();
        memberService.updateMemberAccountStatus(memberId);
        return ResponseEntity.status(HttpStatus.OK).body("회원 공개 여부를 수정했습니다.");
    }

    /** 회원 프로필 수정 */
    @PutMapping("/profile")
    public ResponseEntity<?> updateMemberProfile(@RequestPart("profile") MemberProfileUpdateDto dto,
                                                 @RequestPart(value = "file", required = false) MultipartFile file) {

        Long memberId = SecurityAspect.getCurrentMemberId();
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

    @PublicApi
    @PostMapping("/oauth/login")
    public ResponseEntity<?> oauthLogin(@RequestBody OAuthLoginDto loginDto) {
        String tokens = memberService.oauthLogin(loginDto);
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
    @PutMapping("/promotion/{targetId}")
    public ResponseEntity<?> promotionAdmin(@PathVariable("targetId") Long targetId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        memberService.promotionAdmin(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("관리자로 승인했습니다.");
    }

    /** 관리자 권한 해제 */
    @PutMapping("/relegation/{targetId}")
    public ResponseEntity<?> relegationAdmin(@PathVariable("targetId") Long targetId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        memberService.relegationAdmin(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("관리자 권한을 해제했습니다.");
    }

    /** 게시글 권한 해제 */
    @PutMapping("/suspension/{targetId}")
    public ResponseEntity<?> suspendedArticle(@PathVariable("targetId") Long targetId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        memberService.suspendedArticle(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("게시판 이용 권한을 중지했습니다.");
    }

    /** 게시글 권한 재승인 */
    @PutMapping("/enable/{targetId}")
    public ResponseEntity<?> enableArticle(@PathVariable("targetId") Long targetId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        memberService.enableArticle(targetId);
        return ResponseEntity.status(HttpStatus.OK).body("게시판 이용 권한을 재부여했습니다.");
    }
}