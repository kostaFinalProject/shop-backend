package com.example.shop.repository;

import com.example.shop.domain.instagram.Article;
import com.example.shop.domain.instagram.ArticleCollection;
import com.example.shop.domain.instagram.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCollectionRepository extends JpaRepository<ArticleCollection, Long> {
    boolean existsByMemberAndArticle(Member member, Article article);
}
