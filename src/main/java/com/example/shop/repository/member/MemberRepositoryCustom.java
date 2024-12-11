package com.example.shop.repository.member;

import com.example.shop.domain.instagram.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Article> findArticleByMemberId(Long targetMemberId, Long fromMemberId, Pageable pageable);
    Page<ArticleCollection> findArticleCollectionByMemberId(Long memberId, Pageable pageable);
    Page<Follower> findFollowerByMemberId(Long memberId, Long fromMemberId, Pageable pageable);
    Page<Follower> findFollowingByMemberId(Long memberId, Long fromMemberId, Pageable pageable);
    Page<Follower> findFollowingRequestByMemberId(Long memberId, Pageable pageable);
    Page<Block> findBlockByMemberId(Long memberId, Pageable pageable);
    Page<Member> findMembersByNickName(String nickName, Long fromMemberId, Pageable pageable);
    boolean duplicateMember(String userId, String nickname, String email);
}
