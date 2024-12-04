package com.example.shop.repository;

import com.example.shop.domain.instagram.Article;
import com.example.shop.domain.instagram.ArticleLike;
import com.example.shop.domain.instagram.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Optional<ArticleLike> findByArticleAndMember(Article article, Member member);
}
