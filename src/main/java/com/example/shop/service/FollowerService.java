package com.example.shop.service;

import com.example.shop.domain.instagram.Follower;
import com.example.shop.domain.instagram.Member;
import com.example.shop.dto.instagram.follow.FollowerRequestDto;
import com.example.shop.repository.FollowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final ValidationService validationService;

    @Transactional
    public void follow(Long memberId, FollowerRequestDto dto) {
        if (memberId.equals(dto.getFollowerId())) {
            throw new IllegalArgumentException("자기 자신은 팔로우를 할 수 없습니다.");
        }

        Member followeeMember = validationService.validateMemberById(memberId);

        Member followerMember = validationService.validateMemberById(dto.getFollowerId());

        Follower follower = Follower.createFollower(followeeMember, followerMember);
        followerRepository.save(follower);
    }

    @Transactional
    public void unfollow(Long memberId, FollowerRequestDto dto) {
        if (memberId.equals(dto.getFollowerId())) {
            throw new IllegalArgumentException("자기 자신은 팔로우를 할 수 없습니다.");
        }

        Member followeeMember = validationService.validateMemberById(memberId);
        Member followerMember = validationService.validateMemberById(dto.getFollowerId());

        Follower follower = validationService.validateFollowerByFolloweeAndFollower(followeeMember, followerMember);
        follower.cancelFollower(followeeMember, followerMember);

        followerRepository.delete(follower);
    }
}
