package com.example.shop.controller;

import com.example.shop.aop.SecurityAspect;
import com.example.shop.service.ArticleCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security 적용하면 @PathVariable("memberId") 제거
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/article-collections")
public class ArticleCollectionController {

    private final ArticleCollectionService articleCollectionService;

    /** 게시물 저장 */
    @PostMapping("/{articleId}")
    public ResponseEntity<?> saveArticleCollection(@PathVariable("articleId") Long articleId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        Long articleCollectionId = articleCollectionService.saveArticleCollection(memberId, articleId);
        Map<String, Long> articleCollectionMap = new HashMap<>();
        articleCollectionMap.put("articleId", articleCollectionId);
        return ResponseEntity.status(HttpStatus.OK).body(articleCollectionMap);
    }

    /** 저장된 게시물 삭제 */
    @DeleteMapping("/{articleCollectionId}")
    public ResponseEntity<?> deleteArticleCollection(@PathVariable("articleCollectionId") Long articleCollectionId) {
        articleCollectionService.deleteArticleCollection(articleCollectionId);
        return ResponseEntity.status(HttpStatus.OK).body("게시물 컬렉션에서 삭제했습니다.");
    }
}
