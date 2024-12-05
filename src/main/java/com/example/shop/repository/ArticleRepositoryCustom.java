package com.example.shop.repository;

import com.example.shop.domain.instagram.Article;

import java.util.Optional;

public interface ArticleRepositoryCustom {
    Optional<Article> findArticleWithWriterById(Long articleId);
}
