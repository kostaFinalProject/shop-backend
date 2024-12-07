package com.example.shop.repository.article;

import com.example.shop.domain.instagram.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.shop.domain.instagram.QArticle.article;
import static com.example.shop.domain.instagram.QBlock.block;
import static com.example.shop.domain.instagram.QComment.comment;
import static com.example.shop.domain.instagram.QMember.member;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Article> findArticleWithWriterById(Long memberId, Long articleId) {

        List<Long> excludeMembersId = getExcludedMemberIds(memberId);

        Article result = queryFactory.selectFrom(article)
                .join(article.member, member).fetchJoin()
                .where(article.id.eq(articleId)
                        .and(article.member.id.notIn(excludeMembersId)))
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
    public Page<Article> findAllArticles(Long memberId, Pageable pageable) {

        List<Long> excludeMembersId = getExcludedMemberIds(memberId);

        List<Article> articles = queryFactory.selectFrom(article)
                .where(article.member.id.notIn(excludeMembersId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(article.count())
                .from(article)
                .where(article.member.id.notIn(excludeMembersId));

        return PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<Article> validateArticleAndMemberById(Long articleId, Long memberId) {

        Article result = queryFactory.selectFrom(article)
                .join(article.member, member).fetchJoin()
                .where(article.id.eq(articleId)
                        .and(article.member.id.eq(memberId)))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    /** 차단 기능에 따라 필터링 (내가 차단한 사람, 나를 차단한 사람을 검색 결과에서 제외 시킴) */
    private List<Long> getExcludedMemberIds(Long memberId) {
        List<Long> blockedMemberIds = queryFactory
                .select(block.toMember.id)
                .from(block)
                .where(block.fromMember.id.eq(memberId))
                .fetch();

        List<Long> blockingMemberIds = queryFactory
                .select(block.fromMember.id)
                .from(block)
                .where(block.toMember.id.eq(memberId))
                .fetch();

        return Stream.concat(blockedMemberIds.stream(), blockingMemberIds.stream())
                .distinct()
                .toList();
    }
}