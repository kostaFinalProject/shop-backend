package com.example.shop.repository;

import com.example.shop.domain.instagram.Article;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.shop.domain.instagram.QArticle.article;
import static com.example.shop.domain.instagram.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Article> findArticleByMemberId(Long memberId, Pageable pageable) {

        List<Article> articles = queryFactory.selectFrom(article)
                .join(article.member, member).fetchJoin()
                .where(article.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article)
                .where(article.member.id.eq(memberId));

        return PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne);
    }
}
