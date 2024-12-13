package com.example.shop.controller;

import com.example.shop.aop.SecurityAspect;
import com.example.shop.service.LikeService;
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
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/articles/{articleId}")
    public ResponseEntity<?> saveArticleLike(@PathVariable("articleId") Long articleId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        Long likeId = likeService.saveArticleLike(memberId, articleId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("likeId", likeId));
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<?> saveCommentLike(@PathVariable("commentId") Long commentId) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        Long likeId = likeService.saveCommentLike(memberId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("likeId", likeId));
    }

    @DeleteMapping("/articles/{articleLikeId}")
    public ResponseEntity<?> deleteArticleLike(@PathVariable("articleLikeId") Long articleLikeId) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        likeService.deleteArticleLike(articleLikeId);
        return ResponseEntity.status(HttpStatus.OK).body("좋아요를 취소했습니다.");
    }

    @DeleteMapping("/comments/{commentLikeId}")
    public ResponseEntity<?> deleteCommentLike(@PathVariable("commentLikeId") Long commentLikeId) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        likeService.deleteCommentLike(commentLikeId);
        return ResponseEntity.status(HttpStatus.OK).body("좋아요를 취소했습니다.");
    }
}
