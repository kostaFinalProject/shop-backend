package com.example.shop.repository.member;

import com.example.shop.domain.instagram.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.stream.Stream;

import static com.example.shop.domain.instagram.QArticle.article;
import static com.example.shop.domain.instagram.QArticleCollection.articleCollection;
import static com.example.shop.domain.instagram.QBlock.block;
import static com.example.shop.domain.instagram.QFollower.follower1;
import static com.example.shop.domain.instagram.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Article> findArticleByMemberId(Long memberId, Long fromMemberId, Pageable pageable) {

        List<Long> excludeMembersId = getExcludedMemberIds(fromMemberId);

        List<Article> articles = queryFactory.selectFrom(article)
                .join(article.member, member).fetchJoin()
                .where(article.member.id.eq(memberId)
                        .and(article.articleStatus.eq(ArticleStatus.ACTIVE)
                                .and(article.member.id.notIn(excludeMembersId))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article)
                .where(article.member.id.eq(memberId)
                        .and(article.articleStatus.eq(ArticleStatus.ACTIVE)
                                .and(article.member.id.notIn(excludeMembersId))));

        return PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<ArticleCollection> findArticleCollectionByMemberId(Long memberId, Pageable pageable) {

        List<Long> excludeMembersId = getExcludedMemberIds(memberId);

        List<ArticleCollection> articleCollections = queryFactory.selectFrom(articleCollection)
                .join(articleCollection.member, member).fetchJoin()
                .join(articleCollection.article, article).fetchJoin()
                .where(articleCollection.member.id.eq(memberId)
                        .and(articleCollection.article.articleStatus.eq(ArticleStatus.ACTIVE)
                                .and(article.member.id.notIn(excludeMembersId))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(articleCollection.count())
                .from(articleCollection)
                .where(articleCollection.member.id.eq(memberId)
                        .and(articleCollection.article.articleStatus.eq(ArticleStatus.ACTIVE)
                                .and(article.member.id.notIn(excludeMembersId))));

        return PageableExecutionUtils.getPage(articleCollections, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Follower> findFollowerByMemberId(Long memberId, Pageable pageable) {

        QMember followeeMember = new QMember("followeeMember");
        QMember followerMember = new QMember("followerMember");

        List<Follower> followerList = queryFactory.selectFrom(follower1)
                .join(follower1.followee, followeeMember).fetchJoin()
                .join(follower1.follower, followerMember).fetchJoin()
                .where(follower1.followee.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(follower1.count())
                .from(follower1)
                .where(follower1.followee.id.eq(memberId));

        return PageableExecutionUtils.getPage(followerList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Follower> findFollowingByMemberId(Long memberId, Pageable pageable) {

        QMember followeeMember = new QMember("followeeMember");
        QMember followerMember = new QMember("followerMember");

        List<Follower> followerList = queryFactory.selectFrom(follower1)
                .join(follower1.followee, followeeMember).fetchJoin()
                .join(follower1.follower, followerMember).fetchJoin()
                .where(follower1.follower.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(follower1.count())
                .from(follower1)
                .where(follower1.followee.id.eq(memberId));

        return PageableExecutionUtils.getPage(followerList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Follower> findFollowingRequestByMemberId(Long memberId, Pageable pageable) {

        QMember followeeMember = new QMember("followeeMember");
        QMember followerMember = new QMember("followerMember");

        List<Follower> followerList = queryFactory.selectFrom(follower1)
                .join(follower1.followee, followeeMember).fetchJoin()
                .join(follower1.follower, followerMember).fetchJoin()
                .where(follower1.follower.id.eq(memberId)
                        .and(follower1.followerStatus.eq(FollowerStatus.REQUEST)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(follower1.count())
                .from(follower1)
                .where(follower1.followee.id.eq(memberId)
                        .and(follower1.followerStatus.eq(FollowerStatus.REQUEST)));

        return PageableExecutionUtils.getPage(followerList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Block> findBlockByMemberId(Long memberId, Pageable pageable) {

        QMember fromMember = new QMember("fromMember");
        QMember toMember = new QMember("toMember");

        List<Block> blockList = queryFactory.selectFrom(block)
                .join(block.fromMember, fromMember).fetchJoin()
                .join(block.toMember, toMember).fetchJoin()
                .where(block.fromMember.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(block.count())
                .from(block)
                .where(block.fromMember.id.eq(memberId));

        return PageableExecutionUtils.getPage(blockList, pageable, countQuery::fetchOne);
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