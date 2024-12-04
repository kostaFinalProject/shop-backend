package com.example.shop.controller;

import com.example.shop.dto.instagram.article.ArticleLikeRequestDto;
import com.example.shop.dto.instagram.comment.CommentLikeRequestDto;
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

    @PostMapping("/{memberId}/articles")
    public ResponseEntity<?> toggleArticleLike(@PathVariable("memberId") Long memberId,
                                               @RequestBody ArticleLikeRequestDto dto) {

        boolean toggleArticleLike = likeService.toggleArticleLike(memberId, dto);
        if (toggleArticleLike) {
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 추가");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 취소");
        }
    }

    @PostMapping("/{memberId}/comments")
    public ResponseEntity<?> toggleCommentLike(@PathVariable("memberId") Long memberId,
                                               @RequestBody CommentLikeRequestDto dto) {

        boolean toggleCommentLike = likeService.toggleCommentLike(memberId, dto);
        if (toggleCommentLike) {
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 추가");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 취소");
        }
    }
}
