package com.example.shop.service;

import com.example.shop.domain.instagram.Follower;
import com.example.shop.domain.instagram.FollowerStatus;
import com.example.shop.domain.instagram.Member;
import com.example.shop.repository.follower.FollowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final ValidationService validationService;

    @Transactional
    public Long follow(Long memberId, Long followerId) {
        if (memberId.equals(followerId)) {
            throw new IllegalArgumentException("자기 자신은 팔로우를 할 수 없습니다.");
        }

        Member followeeMember = validationService.validateMemberById(memberId);
        Member followerMember = validationService.validateMemberById(followerId);

        if (followerRepository.existsByFolloweeAndFollower(followeeMember, followerMember)) {
            throw new IllegalArgumentException("이미 팔로우 요청을 보냈거나 팔로우 관계입니다.");
        }

        Follower follower = Follower.createFollower(followeeMember, followerMember);

        if (followerRepository.existsByFolloweeAndFollower(followerMember, followeeMember)) {
            Follower reversFollower = followerRepository.findFollowerWithFolloweeAndFollower(followerMember, followeeMember)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 관계입니다."));

            follower.updateFollowerStatus(FollowerStatus.ACCEPTED);
            reversFollower.updateFollowerStatus(FollowerStatus.ACCEPTED);
        }

        Follower save = followerRepository.save(follower);
        return save.getId();
    }

    /** 맞팔로우 수락 */
    @Transactional
    public void acceptFollower(Long followId) {
        Follower follower = validationService.validateFollowerById(followId);
        Follower acceptedFollower = follower.acceptFollower();
        followerRepository.save(acceptedFollower);
    }

    /** 언팔로우 */
    @Transactional
    public void unfollow(Long followId) {
        Follower follower = validationService.validateFollowerById(followId);
        follower.cancelFollower(follower.getFollowee(), follower.getFollower());

        if (followerRepository.existsByFolloweeAndFollower(follower.getFollower(), follower.getFollowee())) {
            Follower reversFollower = followerRepository.findFollowerWithFolloweeAndFollower(follower.getFollower(), follower.getFollowee())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 관계입니다."));

            reversFollower.updateFollowerStatus(FollowerStatus.REQUEST);
        }

        followerRepository.delete(follower);
    }
}