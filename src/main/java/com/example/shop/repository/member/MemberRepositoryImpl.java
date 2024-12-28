package com.example.shop.repository.member;

import com.example.shop.domain.instagram.*;
import com.example.shop.domain.shop.Item;
import com.example.shop.domain.shop.ItemStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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
import static com.example.shop.domain.instagram.QArticleCollection.articleCollection;
import static com.example.shop.domain.instagram.QArticleItem.articleItem;
import static com.example.shop.domain.instagram.QBlock.block;
import static com.example.shop.domain.instagram.QFollower.follower1;
import static com.example.shop.domain.instagram.QMember.member;
import static com.example.shop.domain.shop.QItem.item;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Article> findArticleByMemberId(Long targetMemberId, Long fromMemberId, Pageable pageable) {
        List<Article> articles;
        JPAQuery<Long> countQuery;

        if (fromMemberId == null) {
            articles = queryFactory.selectFrom(article)
                    .join(article.member, member).fetchJoin()
                    .where(article.member.id.eq(targetMemberId)
                            .and(article.articleStatus.eq(ArticleStatus.ACTIVE))
                            .and(article.member.accountStatus.eq(AccountStatus.PUBLIC))
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            countQuery = queryFactory
                    .select(article.count())
                    .from(article)
                    .where(article.member.id.eq(targetMemberId)
                            .and(article.articleStatus.eq(ArticleStatus.ACTIVE))
                            .and(article.member.accountStatus.eq(AccountStatus.PUBLIC))
                    );
        } else {
            List<Long> excludeMembersId = getExcludedMemberIds(fromMemberId);
            List<Long> followerList = getFollowerList(fromMemberId);

            articles = queryFactory.selectFrom(article)
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

            countQuery = queryFactory
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
        }

        return PageableExecutionUtils.getPage(articles, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Item> findTaggedItemsByMemberId(Long targetMemberId, Long fromMemberId, Pageable pageable) {
        List<Item> items;
        JPAQuery<Long> countQuery;

        if (fromMemberId == null) {
            items = queryFactory.selectDistinct(articleItem.item)
                    .from(article)
                    .join(article.articleItems, articleItem)
                    .join(article.member, member)
                    .where(
                            article.member.id.eq(targetMemberId)
                                    .and(article.articleStatus.eq(ArticleStatus.ACTIVE))
                                    .and(articleItem.item.itemStatus.eq(ItemStatus.ACTIVE)
                                            .or(articleItem.item.itemStatus.eq(ItemStatus.SOLD_OUT)))
                                    .and(article.member.accountStatus.eq(AccountStatus.PUBLIC))
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            countQuery = queryFactory.selectDistinct(articleItem.item.id.count())
                    .from(article)
                    .join(article.articleItems, articleItem)
                    .join(article.member, member)
                    .where(
                            article.member.id.eq(targetMemberId)
                                    .and(article.articleStatus.eq(ArticleStatus.ACTIVE))
                                    .and(articleItem.item.itemStatus.eq(ItemStatus.ACTIVE)
                                            .or(articleItem.item.itemStatus.eq(ItemStatus.SOLD_OUT)))
                                    .and(article.member.accountStatus.eq(AccountStatus.PUBLIC))
                    );
        } else {
            List<Long> excludeMemberIds = getExcludedMemberIds(fromMemberId);
            List<Long> followerList = getFollowerList(fromMemberId);

            items = queryFactory.selectDistinct(articleItem.item)
                    .from(article)
                    .join(article.articleItems, articleItem)
                    .join(article.member, member)
                    .where(
                            article.member.id.eq(targetMemberId)
                                    .and(article.articleStatus.eq(ArticleStatus.ACTIVE))
                                    .and(articleItem.item.itemStatus.eq(ItemStatus.ACTIVE)
                                            .or(articleItem.item.itemStatus.eq(ItemStatus.SOLD_OUT)))
                                    .and(article.member.id.notIn(excludeMemberIds))
                                    .and(
                                            article.member.accountStatus.eq(AccountStatus.PUBLIC)
                                                    .or(article.member.id.in(followerList))
                                                    .or(article.member.id.eq(fromMemberId))
                                    )
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            countQuery = queryFactory.selectDistinct(articleItem.item.id.count())
                    .from(article)
                    .join(article.articleItems, articleItem)
                    .join(article.member, member)
                    .where(
                            article.member.id.eq(targetMemberId)
                                    .and(article.articleStatus.eq(ArticleStatus.ACTIVE))
                                    .and(articleItem.item.itemStatus.eq(ItemStatus.ACTIVE)
                                            .or(articleItem.item.itemStatus.eq(ItemStatus.SOLD_OUT)))
                                    .and(article.member.id.notIn(excludeMemberIds))
                                    .and(
                                            article.member.accountStatus.eq(AccountStatus.PUBLIC)
                                                    .or(article.member.id.in(followerList))
                                                    .or(article.member.id.eq(fromMemberId))
                                    )
                    );
        }

        return PageableExecutionUtils.getPage(items, pageable, countQuery::fetchOne);
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
    public Optional<Member> findByEmailAndProvider(String email, Provider provider) {
        Member result = queryFactory.selectFrom(member)
                .where(member.email.eq(email)
                        .and(member.provider.eq(provider)))
                .fetchOne();

        return Optional.ofNullable(result);
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
                        .and(follower1.follower.id.notIn(excludeMembersId))
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
                        .and(follower1.follower.id.notIn(excludeMembersId))
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
                        .and(follower1.followee.id.notIn(excludeMembersId))
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
                        .and(follower1.followee.id.notIn(excludeMembersId))
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
        List<Long> excludedMemberIds = getExcludedMemberIds(memberId);

        List<Follower> followerList = queryFactory.selectFrom(follower1)
                .join(follower1.followee, followeeMember).fetchJoin()
                .join(follower1.follower, followerMember).fetchJoin()
                .where(follower1.follower.id.eq(memberId)
                        .and(follower1.followerStatus.eq(FollowerStatus.REQUEST))
                        .and(follower1.followee.id.notIn(excludedMemberIds)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(follower1.count())
                .from(follower1)
                .where(follower1.follower.id.eq(memberId)
                        .and(follower1.followerStatus.eq(FollowerStatus.REQUEST))
                        .and(follower1.followee.id.notIn(excludedMemberIds)));

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
    public Page<Member> searchMember(String nickName, Long fromMemberId, Pageable pageable) {
        List<Member> members;
        JPAQuery<Long> countQuery;

        BooleanExpression nicknameCondition = hasNickname(nickName);

        if (fromMemberId == null) {
            members = queryFactory.selectFrom(member)
                    .where(nicknameCondition)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(member.createAt.desc())
                    .fetch();

            countQuery = queryFactory.select(member.count())
                    .from(member)
                    .where(nicknameCondition);
        } else {
            // fromMemberId가 있는 경우 추가 로직 처리
            List<Long> excludeMembersId = getExcludedMemberIds(fromMemberId);
            List<Long> followList = getFollowerList(fromMemberId);
            List<Long> followerList = getFollowList(fromMemberId);

            members = queryFactory.selectFrom(member)
                    .where(
                            nicknameCondition, // 닉네임 조건 추가
                            member.id.notIn(excludeMembersId) // 제외 조건
                    )
                    .orderBy(
                            new CaseBuilder()
                                    .when(member.id.eq(fromMemberId)).then(1)
                                    .when(member.id.in(followList)).then(2)
                                    .when(member.id.in(followerList)).then(3)
                                    .otherwise(4)
                                    .asc(),
                            member.createAt.desc()
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            countQuery = queryFactory.select(member.count())
                    .from(member)
                    .where(
                            nicknameCondition,
                            member.id.notIn(excludeMembersId)
                    );
        }

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

    private BooleanExpression hasNickname(String nickName) {
        if (nickName == null || nickName.isBlank()) {
            return null;
        }

        return member.nickname.containsIgnoreCase(nickName);
    }

    @Override
    public Page<Member> findRequestAdminMember(Pageable pageable) {

        List<Member> members = queryFactory.selectFrom(member)
                .where(member.grade.eq(Grade.NO_AUTHORIZATION_ADMIN))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(member.count())
                .from(member)
                .where(member.grade.eq(Grade.NO_AUTHORIZATION_ADMIN));

        return PageableExecutionUtils.getPage(members, pageable, countQuery::fetchOne);
    }
}