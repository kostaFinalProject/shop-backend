package com.example.shop.repository.follower;

import com.example.shop.domain.instagram.Follower;
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
}
