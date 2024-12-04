package com.example.shop.controller;

import com.example.shop.dto.instagram.follow.FollowerRequestDto;
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

    @PostMapping("/{memberId}")
    public ResponseEntity<?> follow(@PathVariable("memberId") Long memberId,
                                    @RequestBody FollowerRequestDto dto) {
        followerService.follow(memberId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("팔로우");
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> unfollow(@PathVariable("memberId") Long memberId,
                                      @RequestBody FollowerRequestDto dto) {
        followerService.unfollow(memberId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("언팔로우");
    }
}
