package com.example.shop.controller;

import com.example.shop.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{memberId}/{articleId}")
    public ResponseEntity<?> toggleArticleLike(@PathVariable("memberId") Long memberId,
                                               @PathVariable("articleId") Long articleId) {

        boolean toggleArticleLike = likeService.toggleArticleLike(memberId, articleId);
        if (toggleArticleLike) {
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 추가");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 취소");
        }
    }

    @PostMapping("/{memberId}/{commentId}")
    public ResponseEntity<?> toggleCommentLike(@PathVariable("memberId") Long memberId,
                                               @PathVariable("commentId") Long commentId) {

        boolean toggleCommentLike = likeService.toggleCommentLike(memberId, commentId);
        if (toggleCommentLike) {
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 추가");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 취소");
        }
    }
}
