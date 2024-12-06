package com.example.shop.controller;

import com.example.shop.dto.instagram.article.ArticleRequestDto;
import com.example.shop.dto.instagram.comment.CommentRequestDto;
import com.example.shop.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Spring Security 적용하면 @PathVariable("memberId") 제거
 * 남은 작업: 검색 조건에 따른 게시글 검색 (ex: 태그별, 상품 태그별 등), 게시글 및 댓글 신고
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    /** 게시글 생성 */
    @PostMapping("/{memberId}")
    public ResponseEntity<?> saveArticle(@PathVariable("memberId") Long memberId,
                                         @RequestPart("article")ArticleRequestDto dto,
                                         @RequestPart("files") List<MultipartFile> files) {

        articleService.saveArticle(memberId, dto, files);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 생성되었습니다.");
    }

    /** 게시글 댓글 생성 */
    @PostMapping("/{memberId}/{articleId}")
    public ResponseEntity<?> saveComment(@PathVariable("memberId") Long memberId,
                                         @PathVariable("articleId") Long articleId,
                                         @RequestPart("comment") CommentRequestDto dto,
                                         @RequestPart("file") MultipartFile file) {
        articleService.saveComment(memberId, articleId, dto, file);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 댓글이 생성되었습니다.");
    }

    /** 게시글 전체 조회 */
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getArticles(@PathVariable("memberId") Long memberId,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "15") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(articleService.getArticles(memberId, pageable));
    }

    /** 게시글 단건 조회 */
    @GetMapping("/{memberId}/{articleId}")
    public ResponseEntity<?> getArticle(@PathVariable("memberId") Long memberId,
                                        @PathVariable("articleId") Long articleId) {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.getArticle(memberId, articleId));
    }

    /** 게시글 댓글 조회 */
    @GetMapping("/{memberId}/{articleId}/comments")
    public ResponseEntity<?> getArticleComments(@PathVariable("memberId") Long memberId,
                                                @PathVariable("articleId") Long articleId,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(articleService.getComments(memberId, articleId, pageable));
    }

    /** 게시글 수정 (작성자 & 관리자 등급만 가능) */
    @PutMapping("/{memberId}/{articleId}")
    public ResponseEntity<?> updateArticle(@PathVariable("memberId") Long memberId,
                                           @PathVariable("articleId") Long articleId,
                                           @RequestPart("article")ArticleRequestDto dto,
                                           @RequestPart("files") List<MultipartFile> files) {

        articleService.updateArticle(memberId, articleId, dto, files);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 수정되었습니다.");
    }

    /** 게시글 활성화 (관리자 기능) */
    @PutMapping("/{memberId}/{articleId}/active")
    public ResponseEntity<?> activeArticle(@PathVariable("memberId") Long memberId,
                                             @PathVariable("articleId") Long articleId) {

        articleService.activeArticle(memberId, articleId);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 활성화 되었습니다.");
    }

    /** 게시글 비활성화 (관리자 기능) */
    @PutMapping("/{memberId}/{articleId}/inactive")
    public ResponseEntity<?> inactiveArticle(@PathVariable("memberId") Long memberId,
                                             @PathVariable("articleId") Long articleId) {

        articleService.inactiveArticle(memberId, articleId);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 비활성화 되었습니다.");
    }

    /** 게시글 삭제 (작성자 or 관리자만 가능) */
    @DeleteMapping("/{memberId}/{articleId}")
    public ResponseEntity<?> deleteArticle(@PathVariable("memberId") Long memberId,
                                           @PathVariable("articleId") Long articleId) {

        articleService.deleteArticle(memberId, articleId);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 삭제되었습니다.");
    }
}
