package com.example.shop.repository.articlelike;

import com.example.shop.domain.instagram.ArticleLike;

import java.util.Optional;

public interface ArticleLikeRepositoryCustom {
    boolean existsByArticleAndMemberById(Long articleId, Long memberId);
    Long findArticleLikeIdByArticleAndMember(Long articleId, Long memberId);
    Optional<ArticleLike> findArticleLikeWithArticleAndMemberById(Long articleLikeId);
}