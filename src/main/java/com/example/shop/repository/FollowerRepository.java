package com.example.shop.repository;

import com.example.shop.domain.instagram.Follower;
import com.example.shop.domain.instagram.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    Optional<Follower> findByFolloweeAndFollower(Member followee, Member follower);
}
