package com.example.shop.repository.articlelike;

import com.example.shop.domain.instagram.ArticleLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.example.shop.domain.instagram.QArticle.article;
import static com.example.shop.domain.instagram.QArticleLike.articleLike;
import static com.example.shop.domain.instagram.QMember.member;

@RequiredArgsConstructor
public class ArticleLikeRepositoryImpl implements ArticleLikeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByArticleAndMemberById(Long articleId, Long memberId) {
        return queryFactory.selectOne()
                .from(articleLike)
                .where(articleLike.article.id.eq(articleId)
                        .and(articleLike.member.id.eq(memberId)))
                .fetchFirst() != null;
    }

    @Override
    public Long findArticleLikeIdByArticleAndMember(Long articleId, Long memberId) {
        if (memberId == null) {
            return null;
        }

        return queryFactory
                .select(articleLike.id)
                .from(articleLike)
                .where(articleLike.article.id.eq(articleId)
                        .and(articleLike.member.id.eq(memberId)))
                .fetchFirst();
    }

    @Override
    public Optional<ArticleLike> findArticleLikeById(Long articleLikeId) {

        ArticleLike result = queryFactory.selectFrom(articleLike)
                .where(articleLike.id.eq(articleLikeId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
