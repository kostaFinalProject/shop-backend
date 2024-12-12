package com.example.shop.controller;

import com.example.shop.aop.PublicApi;
import com.example.shop.aop.SecurityAspect;
import com.example.shop.config.CustomUserDetails;
import com.example.shop.dto.instagram.article.ArticleRequestDto;
import com.example.shop.dto.instagram.comment.CommentRequestDto;
import com.example.shop.service.ArticleService;
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
    private final ValidationService validationService;

    /** 게시글 생성 */
    @PostMapping
    public ResponseEntity<?> saveArticle(@RequestPart("article")ArticleRequestDto dto,
                                         @RequestPart("files") List<MultipartFile> files) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        articleService.saveArticle(memberId, dto, files);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 생성되었습니다.");
    }

    /** 게시글 댓글 생성 */
    @PostMapping("/{articleId}")
    public ResponseEntity<?> saveComment(@PathVariable("articleId") Long articleId,
                                         @RequestPart("comment") CommentRequestDto dto,
                                         @RequestPart("file") MultipartFile file) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        articleService.saveComment(memberId, articleId, dto, file);
        return ResponseEntity.status(HttpStatus.OK).body("게시글 댓글이 생성되었습니다.");
    }

    /** 게시글 전체 조회 */
    @PublicApi
    @GetMapping
    public ResponseEntity<?> getArticles(@RequestParam(value = "tag", required = false) String tag,
                                         @RequestParam(value = "item", required = false) String item,
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

        System.out.println("memberId = " + memberId);

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(articleService.getArticles(memberId, tag, item, pageable));
    }

    /** 게시글 단건 조회 */
    @PublicApi
    @GetMapping("/{articleId}")
    public ResponseEntity<?> getArticle(@PathVariable("articleId") Long articleId) {

        Long memberId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            if (userDetails.getUsername() != null) {
                String userId = userDetails.getUsername();
                memberId = validationService.validateMemberByUserId(userId).getId();
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(articleService.getArticle(memberId, articleId));
    }

    /** 게시글 댓글 조회 */
    @PublicApi
    @GetMapping("/comments/{articleId}")
    public ResponseEntity<?> getArticleComments(@PathVariable("articleId") Long articleId,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "20") int size) {

        Long memberId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            if (userDetails.getUsername() != null) {
                String userId = userDetails.getUsername();
                memberId = validationService.validateMemberByUserId(userId).getId();
            }
        }
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
    @PutMapping("/active/{memberId}/{articleId}")
    public ResponseEntity<?> activeArticle(@PathVariable("memberId") Long memberId,
                                             @PathVariable("articleId") Long articleId) {

        articleService.activeArticle(memberId, articleId);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 활성화 되었습니다.");
    }

    /** 게시글 비활성화 (관리자 기능) */
    @PutMapping("/inactive/{memberId}/{articleId}")
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
