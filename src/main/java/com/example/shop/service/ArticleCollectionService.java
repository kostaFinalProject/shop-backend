package com.example.shop.service;

import com.example.shop.domain.instagram.Article;
import com.example.shop.domain.instagram.ArticleCollection;
import com.example.shop.domain.instagram.Member;
import com.example.shop.dto.instagram.article.ArticleCollectionRequestDto;
import com.example.shop.repository.ArticleCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleCollectionService {

    private final ArticleCollectionRepository articleCollectionRepository;
    private final ValidationService validationService;

    @Transactional
    public void saveArticleCollection(Long memberId, ArticleCollectionRequestDto dto) {
        Member member = validationService.validateMemberById(memberId);

        Article article = validationService.validateArticleById(dto.getArticleId());

        if (validationService.validateArticleCollectionByArticleAndMember(member, article)) {
            throw new IllegalStateException("이미 저장된 게시글입니다.");
        }

        ArticleCollection articleCollection = ArticleCollection.createArticleCollection(member, article);
        articleCollectionRepository.save(articleCollection);
    }
}
