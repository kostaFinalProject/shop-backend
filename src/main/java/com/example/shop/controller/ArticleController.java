package com.example.shop.controller;

import com.example.shop.dto.instagram.ArticleRequestDto;
import com.example.shop.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/{memberId}")
    public ResponseEntity<?> saveArticle(@PathVariable("memberId") Long memberId,
                                         @RequestPart("article")ArticleRequestDto dto,
                                         @RequestPart("files") List<MultipartFile> files) {

        articleService.saveArticle(memberId, dto, files);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 성공적으로 생성되었습니다.");
    }

    @PutMapping("/{memberId}/{articleId}")
    public ResponseEntity<?> updateArticle(@PathVariable("memberId") Long memberId,
                                           @PathVariable("articleId") Long articleId,
                                           @RequestPart("article")ArticleRequestDto dto,
                                           @RequestPart("files") List<MultipartFile> files) {

        articleService.updateArticle(memberId, articleId, dto, files);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 성공적으로 수정되었습니다.");
    }
}
