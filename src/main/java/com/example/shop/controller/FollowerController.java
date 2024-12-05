package com.example.shop.controller;

import com.example.shop.service.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/followers")
public class FollowerController {

    private final FollowerService followerService;

    @PostMapping("/{memberId}/{followerId}")
    public ResponseEntity<?> follow(@PathVariable("memberId") Long memberId,
                                    @PathVariable("followerId") Long followerId) {
        followerService.follow(memberId, followerId);
        return ResponseEntity.status(HttpStatus.OK).body("팔로우");
    }

    @DeleteMapping("/{memberId}/{followerId}")
    public ResponseEntity<?> unfollow(@PathVariable("memberId") Long memberId,
                                      @PathVariable("followerId") Long followerId) {
        followerService.unfollow(memberId, followerId);
        return ResponseEntity.status(HttpStatus.OK).body("언팔로우");
    }
}
