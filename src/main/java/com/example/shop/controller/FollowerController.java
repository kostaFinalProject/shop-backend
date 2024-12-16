package com.example.shop.controller;

import com.example.shop.aop.SecurityAspect;
import com.example.shop.service.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Spring Security 적용하면 @PathVariable("memberId") 제거
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/followers")
public class FollowerController {

    private final FollowerService followerService;

    /** 팔로우 요청 */
    @PostMapping("/{followerId}")
    public ResponseEntity<?> followRequest(@PathVariable("followerId") Long followerId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        Long savedFollowerId = followerService.follow(memberId, followerId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("followerId", savedFollowerId));
    }

    /** 팔로우 요청 수락 */
    @PutMapping("/{followId}")
    public ResponseEntity<?> followAccept(@PathVariable("followId") Long followerId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        followerService.acceptFollower(followerId);
        return ResponseEntity.status(HttpStatus.OK).body("팔로우 요청을 수락했습니다.");
    }

    /** 언팔로우 */
    @DeleteMapping("/{followId}")
    public ResponseEntity<?> unfollow(@PathVariable("followId") Long followId) {
        followerService.unfollow(followId);
        return ResponseEntity.status(HttpStatus.OK).body("언팔로우");
    }
}
