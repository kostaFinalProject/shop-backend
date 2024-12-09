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

    @Enumerated(EnumType.STRING)
    private FollowerStatus followerStatus;

    @Builder
    private Follower(Member followee, Member follower) {
        this.followee = followee;
        this.follower = follower;
        this.followerStatus = FollowerStatus.REQUEST;
    }

    /** 팔로우 요청 */
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

    public void updateFollowerStatus(FollowerStatus followerStatus) {
        this.followerStatus = followerStatus;
    }

    /** 맞팔로우 수락 */
    public Follower acceptFollower() {
        followee.setFollowees(1);
        follower.setFollowers(1);
        this.followerStatus = FollowerStatus.ACCEPTED;

        Follower newFollower = Follower.builder().followee(follower).follower(followee).build();
        newFollower.followerStatus = FollowerStatus.ACCEPTED;

        return newFollower;
    }


}
