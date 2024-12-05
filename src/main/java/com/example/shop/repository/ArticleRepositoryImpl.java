package com.example.shop.repository;

import com.example.shop.domain.instagram.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.example.shop.domain.instagram.QArticle.article;
import static com.example.shop.domain.instagram.QComment.comment;
import static com.example.shop.domain.instagram.QMember.member;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Article> findArticleWithWriterById(Long articleId) {

        Article result = queryFactory.selectFrom(article)
                .join(article.member, member).fetchJoin()
                .where(article.id.eq(articleId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<Comment> findCommentsByArticleId(Long articleId, Pageable pageable) {

        List<Comment> comments = queryFactory.selectFrom(comment)
                .join(comment.article, article).fetchJoin()
                .where(comment.id.eq(articleId)
                        .and(comment.parentComment.isNull())
                        .and(comment.commentStatus.ne(CommentStatus.DELETED)))
                .orderBy(comment.likes.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(comment.count()).from(comment)
                .where(comment.article.id.eq(articleId)
                        .and(comment.parentComment.isNull())
                        .and(comment.commentStatus.ne(CommentStatus.DELETED)));

        return PageableExecutionUtils.getPage(comments, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Article> findAllArticles(Pageable pageable) {

        List<Article> articles = queryFactory.selectFrom(article)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(article.count()).from(article);

        return PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<Article> validationArticleAndMemberById(Long articleId, Long memberId) {

        Article result = queryFactory.selectFrom(article)
                .join(article.member, member).fetchJoin()
                .where(article.id.eq(articleId)
                        .and(article.member.id.eq(memberId)))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
