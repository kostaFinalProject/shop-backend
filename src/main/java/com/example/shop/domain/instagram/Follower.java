package com.example.shop.domain.instagram;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 팔로우/팔로워
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Follower {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follower_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_member_id")
    private Member followee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_member_id")
    private Member follower;

    @Builder
    private Follower(Member followee, Member follower) {
        this.followee = followee;
        this.follower = follower;
    }

    /** 팔로우 */
    public static Follower createFollower(Member followee, Member follower) {
        followee.setFollowers(1);
        follower.setFollowees(1);
        return Follower.builder().followee(followee).follower(follower).build();
    }

    /** 언팔로우 */
    public void cancelFollower(Member followee, Member follower) {
        followee.setFollowers(-1);
        follower.setFollowees(-1);
    }
}
