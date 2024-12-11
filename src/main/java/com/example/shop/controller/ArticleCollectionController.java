package com.example.shop.controller;

import com.example.shop.service.ArticleCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Spring Security 적용하면 @PathVariable("memberId") 제거
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/article-collections")
public class ArticleCollectionController {

    private final ArticleCollectionService articleCollectionService;

    /** 게시물 저장 */
    @PostMapping("/{memberId}/{articleId}")
    public ResponseEntity<?> saveArticleCollection(@PathVariable("memberId") Long memberId,
                                                   @PathVariable("articleId") Long articleId) {
        articleCollectionService.saveArticleCollection(memberId, articleId);
        return ResponseEntity.status(HttpStatus.OK).body("게시물 컬렉션에 추가했습니다.");
    }

    /** 저장된 게시물 삭제 */
    @DeleteMapping("/{articleCollectionId}")
    public ResponseEntity<?> deleteArticleCollection(@PathVariable("articleCollectionId") Long articleCollectionId) {
        articleCollectionService.deleteArticleCollection(articleCollectionId);
        return ResponseEntity.status(HttpStatus.OK).body("게시물 컬렉션에서 삭제했습니다.");
    }
}
