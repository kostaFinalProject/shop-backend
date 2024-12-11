package com.example.shop.repository.member;

import com.example.shop.domain.instagram.*;
import com.querydsl.core.types.dsl.CaseBuilder;
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
    public Page<Article> findArticleByMemberId(Long targetMemberId, Long fromMemberId, Pageable pageable) {

        List<Long> excludeMembersId = getExcludedMemberIds(fromMemberId);
        List<Long> followerList = getFollowerList(fromMemberId);

        List<Article> articles = queryFactory.selectFrom(article)
                .join(article.member, member).fetchJoin()
                .where(article.member.id.eq(targetMemberId)
                        .and(article.articleStatus.eq(ArticleStatus.ACTIVE)
                                .and(article.member.id.notIn(excludeMembersId))
                                .and(
                                        article.member.accountStatus.eq(AccountStatus.PUBLIC)
                                                .or(article.member.id.in(followerList))
                                                .or(article.member.id.eq(fromMemberId))
                                )
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article)
                .where(article.member.id.eq(targetMemberId)
                        .and(article.articleStatus.eq(ArticleStatus.ACTIVE)
                                .and(article.member.id.notIn(excludeMembersId))
                                .and(
                                        article.member.accountStatus.eq(AccountStatus.PUBLIC)
                                                .or(article.member.id.in(followerList))
                                                .or(article.member.id.eq(fromMemberId))
                                )
                        )
                );

        return PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne);
    }

    @Override
    public boolean duplicateMember(String userId, String nickname, String email) {
        return queryFactory.selectOne()
                .from(member)
                .where(
                        member.userId.eq(userId)
                                .or(member.nickname.eq(nickname))
                                .or(member.email.eq(email)
                                        .and(member.provider.eq(Provider.GENERAL))
                                )
                )
                .fetchFirst() != null;
    }

    @Override
    public Page<ArticleCollection> findArticleCollectionByMemberId(Long memberId, Pageable pageable) {

        List<Long> excludeMembersId = getExcludedMemberIds(memberId);
        List<Long> followerList = getFollowerList(memberId);

        List<ArticleCollection> articleCollections = queryFactory.selectFrom(articleCollection)
                .join(articleCollection.member, member).fetchJoin()
                .join(articleCollection.article, article).fetchJoin()
                .where(article.member.id.eq(memberId)
                        .and(article.articleStatus.eq(ArticleStatus.ACTIVE)
                                .and(article.member.id.notIn(excludeMembersId))
                                .and(article.member.accountStatus.eq(AccountStatus.PUBLIC)
                                        .or(article.member.id.in(followerList)))
                                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(articleCollection.count())
                .from(articleCollection)
                .where(article.member.id.eq(memberId)
                        .and(article.articleStatus.eq(ArticleStatus.ACTIVE)
                                .and(article.member.id.notIn(excludeMembersId))
                                .and(article.member.accountStatus.eq(AccountStatus.PUBLIC)
                                        .or(article.member.id.in(followerList)))
                        ));

        return PageableExecutionUtils.getPage(articleCollections, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Follower> findFollowerByMemberId(Long targetMemberId, Long fromMemberId, Pageable pageable) {
        QMember followeeMember = new QMember("followeeMember");
        QMember followerMember = new QMember("followerMember");

        List<Long> excludeMembersId = getExcludedMemberIds(fromMemberId);
        List<Long> followList = getFollowerList(fromMemberId);

        List<Follower> followerList = queryFactory.selectFrom(follower1)
                .join(follower1.followee, followeeMember).fetchJoin()
                .join(follower1.follower, followerMember).fetchJoin()
                .where(follower1.followee.id.eq(targetMemberId)
                        .and(follower1.followee.id.notIn(excludeMembersId))
                        .and(
                                follower1.followee.accountStatus.eq(AccountStatus.PUBLIC)
                                        .or(follower1.followee.id.in(followList))
                                        .or(follower1.followee.id.eq(fromMemberId))
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(follower1.count())
                .from(follower1)
                .where(follower1.followee.id.eq(targetMemberId)
                        .and(follower1.followee.id.notIn(excludeMembersId))
                        .and(
                                follower1.followee.accountStatus.eq(AccountStatus.PUBLIC)
                                        .or(follower1.followee.id.in(followList))
                                        .or(follower1.followee.id.eq(fromMemberId))
                        )
                );

        return PageableExecutionUtils.getPage(followerList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Follower> findFollowingByMemberId(Long targetMemberId, Long fromMemberId, Pageable pageable) {
        QMember followeeMember = new QMember("followeeMember");
        QMember followerMember = new QMember("followerMember");

        List<Long> excludeMembersId = getExcludedMemberIds(fromMemberId);
        List<Long> followList = getFollowerList(fromMemberId);

        List<Follower> followerList = queryFactory.selectFrom(follower1)
                .join(follower1.followee, followeeMember).fetchJoin()
                .join(follower1.follower, followerMember).fetchJoin()
                .where(follower1.follower.id.eq(targetMemberId)
                        .and(follower1.follower.id.notIn(excludeMembersId))
                        .and(
                                followeeMember.accountStatus.eq(AccountStatus.PUBLIC)
                                        .or(follower1.follower.id.in(followList))
                                        .or(follower1.follower.id.eq(fromMemberId))
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(follower1.count())
                .from(follower1)
                .where(follower1.follower.id.eq(targetMemberId)
                        .and(follower1.follower.id.notIn(excludeMembersId))
                        .and(
                                followeeMember.accountStatus.eq(AccountStatus.PUBLIC)
                                        .or(follower1.follower.id.in(followList))
                                        .or(follower1.follower.id.eq(fromMemberId))
                        )
                );

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

    @Override
    public Page<Member> findMembersByNickName(String nickName, Long fromMemberId, Pageable pageable) {
        List<Long> excludeMembersId = getExcludedMemberIds(fromMemberId);
        List<Long> followList = getFollowerList(fromMemberId);
        List<Long> followerList = getFollowList(fromMemberId);

        List<Member> members = queryFactory.selectFrom(member)
                .where(
                        member.nickname.containsIgnoreCase(nickName)
                                .and(member.id.notIn(excludeMembersId))
                                .and(member.id.ne(fromMemberId))
                )
                .orderBy(
                        new CaseBuilder()
                                .when(member.id.in(followList)).then(1)
                                .when(member.id.in(followerList)).then(2)
                                .otherwise(3)
                                .asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(member.count())
                .from(member)
                .where(
                        member.nickname.containsIgnoreCase(nickName)
                                .and(member.id.notIn(excludeMembersId))
                );

        return PageableExecutionUtils.getPage(members, pageable, countQuery::fetchOne);
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

    /** 사용자를 팔로우하는 사용자 */
    private List<Long> getFollowList(Long fromMemberId) {
        return queryFactory
                .select(follower1.followee.id)
                .from(follower1)
                .where(follower1.follower.id.eq(fromMemberId))
                .fetch();
    }

    /** 비공개 계정이면 팔로우 해야 조회 가능
     * 사용자가 팔로우하는 사용자
     */
    private List<Long> getFollowerList(Long memberId) {
        return queryFactory
                .select(follower1.follower.id)
                .from(follower1)
                .where(follower1.followee.id.eq(memberId))
                .fetch();
    }
}