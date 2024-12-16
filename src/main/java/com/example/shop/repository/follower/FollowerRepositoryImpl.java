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
    public boolean existsFollowerWithFolloweeAndFollower(Long followeeId, Long followerId) {
        if (followeeId == null) {
            return false;
        }

        QMember followeeMember = new QMember("followeeMember");
        QMember followerMember = new QMember("followerMember");

        return queryFactory.select(follower1)
                .from(follower1)
                .join(follower1.followee, followeeMember).fetchJoin()
                .join(follower1.follower, followerMember).fetchJoin()
                .where(follower1.followee.id.eq(followerId)
                        .and(follower1.follower.id.eq(followeeId)))
                .fetchOne() != null;
    }

    @Override
    public Follower findByFolloweeIdAndFollowerId(Long followeeId, Long followerId) {
        if (followeeId == null) {
            return null;
        }

        return queryFactory.selectFrom(follower1)
                .where(follower1.followee.id.eq(followeeId)
                        .and(follower1.follower.id.eq(followerId)))
                .fetchFirst();
    }
}
