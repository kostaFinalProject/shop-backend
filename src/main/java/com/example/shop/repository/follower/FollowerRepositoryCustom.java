package com.example.shop.repository.follower;

import com.example.shop.domain.instagram.Follower;
import com.example.shop.domain.instagram.Member;

import java.util.Optional;

public interface FollowerRepositoryCustom {
    Optional<Follower> findFollowerWithFolloweeAndFollowerById(Long followerId);
    Optional<Follower> findFollowerWithFolloweeAndFollower(Member followee, Member follower);
    boolean existsFollowerWithFolloweeAndFollower(Long followeeId, Long followerId);
    Follower findByFolloweeIdAndFollowerId(Long followeeId, Long followerId);
}
