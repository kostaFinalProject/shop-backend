package com.example.shop.repository.follower;

import com.example.shop.domain.instagram.Follower;

import java.util.Optional;

public interface FollowerRepositoryCustom {
    Optional<Follower> findFollowerWithFolloweeAndFollowerById(Long followerId);
}
