package com.example.shop.repository.article;

import com.example.shop.domain.instagram.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
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
import static com.example.shop.domain.instagram.QArticleItem.articleItem;
import static com.example.shop.domain.instagram.QArticleTag.articleTag;
import static com.example.shop.domain.instagram.QBlock.block;
import static com.example.shop.domain.instagram.QComment.comment;
import static com.example.shop.domain.instagram.QFollower.follower1;
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
    public Page<Comment> findCommentsByArticleId(Long memberId, Long articleId, Pageable pageable) {

        List<Comment> comments;
        JPAQuery<Long> countQuery;

        if (memberId == null) {
            comments = queryFactory.selectFrom(comment)
                    .join(comment.article, article).fetchJoin()
                    .where(comment.article.id.eq(articleId)
                            .and(comment.parentComment.isNull())
                            .and(comment.commentStatus.ne(CommentStatus.DELETED)))
                    .orderBy(comment.likes.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            countQuery = queryFactory.select(comment.count()).from(comment)
                    .where(comment.article.id.eq(articleId)
                            .and(comment.parentComment.isNull())
                            .and(comment.commentStatus.ne(CommentStatus.DELETED)));
        } else {
            List<Long> excludedMemberIds = getExcludedMemberIds(memberId);

            comments = queryFactory.selectFrom(comment)
                    .join(comment.article, article).fetchJoin()
                    .where(comment.article.id.eq(articleId)
                            .and(comment.parentComment.isNull())
                            .and(comment.commentStatus.ne(CommentStatus.DELETED))
                            .and(comment.member.id.notIn(excludedMemberIds)))
                    .orderBy(comment.likes.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            countQuery = queryFactory.select(comment.count()).from(comment)
                    .where(comment.article.id.eq(articleId)
                            .and(comment.parentComment.isNull())
                            .and(comment.commentStatus.ne(CommentStatus.DELETED))
                            .and(comment.member.id.notIn(excludedMemberIds)));
        }

        return PageableExecutionUtils.getPage(comments, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Article> searchArticles(Long memberId, String tag, Long itemId, Pageable pageable) {
        BooleanExpression baseCondition;
        BooleanExpression commonCondition = article.articleStatus.eq(ArticleStatus.ACTIVE);

        if (memberId != null) {
            List<Long> excludeMembersId = getExcludedMemberIds(memberId);
            List<Long> accessiblePrivateMemberIds = getFollowerList(memberId);

            baseCondition = commonCondition
                    .and(article.member.id.notIn(excludeMembersId))
                    .and(article.member.accountStatus.eq(AccountStatus.PUBLIC)
                            .or(article.member.id.in(accessiblePrivateMemberIds))
                            .or(article.member.id.eq(memberId)));
        } else {
            baseCondition = commonCondition.and(article.member.accountStatus.eq(AccountStatus.PUBLIC));
        }

        BooleanExpression tagCondition = hasTag(tag);
        BooleanExpression itemCondition = hasItem(itemId);
        BooleanExpression combinedConditions = combineConditions(baseCondition, tagCondition, itemCondition);

        List<Article> articles = queryFactory.selectFrom(article)
                .join(article.member, member)
                .where(combinedConditions)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(article.count())
                .from(article)
                .where(combinedConditions);

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

    /** 비공개 계정이면 팔로우 해야 조회 가능 */
    private List<Long> getFollowerList(Long memberId) {
        return queryFactory
                .select(follower1.follower.id)
                .from(follower1)
                .where(follower1.followee.id.eq(memberId))
                .fetch();
    }

    private BooleanExpression hasTag(String tag) {
        if (tag == null || tag.isBlank()) {
            return null;
        }

        return article.articleTags.any().tag.tag.containsIgnoreCase(tag);
    }

    private BooleanExpression hasItem(Long itemId) {
        if (itemId == null) {
            return null;
        }

        return article.articleItems.any().item.id.eq(itemId);
    }

    private BooleanExpression combineConditions(BooleanExpression... conditions) {
        BooleanExpression result = null;
        for (BooleanExpression condition : conditions) {
            if (condition != null) {
                result = (result == null) ? condition : result.and(condition);
            }
        }
        return result;
    }
}