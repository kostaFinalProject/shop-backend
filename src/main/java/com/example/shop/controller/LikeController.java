package com.example.shop.controller;

import com.example.shop.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Spring Security 적용하면 @PathVariable("memberId") 제거
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/articles/{memberId}/{articleId}")
    public ResponseEntity<?> saveArticleLike(@PathVariable("memberId") Long memberId,
                                             @PathVariable("articleId") Long articleId) {
        likeService.saveArticleLike(memberId, articleId);
        return ResponseEntity.status(HttpStatus.OK).body("좋아요를 눌렀습니다.");
    }

    @PostMapping("/comments/{memberId}/{commentId}")
    public ResponseEntity<?> saveCommentLike(@PathVariable("memberId") Long memberId,
                                             @PathVariable("commentId") Long commentId) {

        likeService.saveCommentLike(memberId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("좋아요를 눌렀습니다.");
    }

    @DeleteMapping("/articles/{articleLikeId}")
    public ResponseEntity<?> deleteArticleLike(@PathVariable("articleLikeId") Long articleLikeId) {
        likeService.deleteArticleLike(articleLikeId);
        return ResponseEntity.status(HttpStatus.OK).body("좋아요를 취소했습니다.");
    }

    @DeleteMapping("/comments/{commentLikeId}")
    public ResponseEntity<?> deleteCommentLike(@PathVariable("commentLikeId") Long commentLikeId) {
        likeService.deleteCommentLike(commentLikeId);
        return ResponseEntity.status(HttpStatus.OK).body("좋아요를 취소했습니다.");
    }
}
