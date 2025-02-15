package com.example.shop.repository.article;

import com.example.shop.domain.instagram.Article;
import com.example.shop.domain.instagram.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ArticleRepositoryCustom {
    Optional<Article> findArticleWithWriterById(Long articleId);
    Page<Comment> findCommentsByArticleId(Long memberId, Long articleId, Pageable pageable);
    Page<Article> searchArticles(Long memberId, String tag, Long itemId, String content, String sortType, Pageable pageable);
    Optional<Article> validateArticleAndMemberById(Long articleId, Long memberId);
}
