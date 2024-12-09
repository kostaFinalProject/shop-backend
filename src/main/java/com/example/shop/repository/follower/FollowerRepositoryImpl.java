package com.example.shop.repository.follower;

import com.example.shop.domain.instagram.Follower;
import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.instagram.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.example.shop.domain.instagram.QFollower.follower1;

@RequiredArgsConstructor
public class FollowerRepositoryImpl implements FollowerRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Follower> findFollowerWithFolloweeAndFollowerById(Long followerId) {

        QMember followeeMember = new QMember("followeeMember");
        QMember followerMember = new QMember("followerMember");

        Follower result = queryFactory.selectFrom(follower1)
                .join(follower1.followee, followeeMember).fetchJoin()
                .join(follower1.follower, followerMember).fetchJoin()
                .where(follower1.id.eq(followerId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Follower> findFollowerWithFolloweeAndFollower(Member followee, Member follower) {
        QMember followeeMember = new QMember("followeeMember");
        QMember followerMember = new QMember("followerMember");

        Follower result = queryFactory.selectFrom(follower1)
                .join(follower1.followee, followeeMember).fetchJoin()
                .join(follower1.follower, followerMember).fetchJoin()
                .where(follower1.followee.eq(followee)
                        .and(follower1.follower.eq(followerMember)))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsFollowerWithFolloweeAndFollower(Member followee, Member follower) {
        QMember followeeMember = new QMember("followeeMember");
        QMember followerMember = new QMember("followerMember");

        return queryFactory.selectOne()
                .from(follower1)
                .join(follower1.followee, followeeMember).fetchJoin()
                .join(follower1.follower, followerMember).fetchJoin()
                .where(follower1.followee.eq(follower)
                        .and(follower1.follower.eq(followee)))
                .fetchOne() != null;
    }
}
