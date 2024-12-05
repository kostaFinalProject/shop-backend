package com.example.shop.repository;

import com.example.shop.domain.instagram.Article;
import com.example.shop.domain.instagram.QArticle;
import com.example.shop.domain.instagram.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Article> findArticleWithWriterById(Long articleId) {
        QArticle qArticle = QArticle.article;
        QMember qMember = QMember.member;

        Article article = queryFactory.selectFrom(qArticle)
                .join(qArticle.member, qMember).fetchJoin()
                .where(qArticle.id.eq(articleId))
                .fetchOne();

        return Optional.ofNullable(article);
    }
}
