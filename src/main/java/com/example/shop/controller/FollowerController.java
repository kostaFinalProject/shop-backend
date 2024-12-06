package com.example.shop.controller;

import com.example.shop.service.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Spring Security 적용하면 @PathVariable("memberId") 제거
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/followers")
public class FollowerController {

    private final FollowerService followerService;

    /** 팔로우 */
    @PostMapping("/{memberId}/{followerId}")
    public ResponseEntity<?> follow(@PathVariable("memberId") Long memberId,
                                    @PathVariable("followerId") Long followerId) {
        followerService.follow(memberId, followerId);
        return ResponseEntity.status(HttpStatus.OK).body("팔로우");
    }

    /** 언팔로우 */
    @DeleteMapping("/{followId}")
    public ResponseEntity<?> unfollow(@PathVariable("followId") Long followId) {
        followerService.unfollow(followId);
        return ResponseEntity.status(HttpStatus.OK).body("언팔로우");
    }
}
