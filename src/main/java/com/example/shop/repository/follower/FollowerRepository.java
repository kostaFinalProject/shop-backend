package com.example.shop.repository.follower;

import com.example.shop.domain.instagram.Follower;
import com.example.shop.domain.instagram.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long>, FollowerRepositoryCustom {
    Optional<Follower> findByFolloweeAndFollower(Member followee, Member follower);
}
