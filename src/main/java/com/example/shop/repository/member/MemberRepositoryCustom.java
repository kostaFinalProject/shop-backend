package com.example.shop.repository.member;

import com.example.shop.domain.instagram.*;
import com.example.shop.domain.shop.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Page<Article> findArticleByMemberId(Long targetMemberId, Long fromMemberId, Pageable pageable);
    Page<ArticleCollection> findArticleCollectionByMemberId(Long memberId, Pageable pageable);
    Page<Follower> findFollowerByMemberId(Long targetMemberId, Long fromMemberId, Pageable pageable);
    Page<Follower> findFollowingByMemberId(Long targetMemberId, Long fromMemberId, Pageable pageable);
    Page<Follower> findFollowingRequestByMemberId(Long memberId, Pageable pageable);
    Page<Block> findBlockByMemberId(Long memberId, Pageable pageable);
    Page<Member> findMembersByNickName(String nickName, Long fromMemberId, Pageable pageable);
    boolean duplicateMember(String userId, String nickname, String email);
    Optional<Member> findByEmailAndProvider(String email, Provider provider);
    Page<Item> findTaggedItemsByMemberId(Long targetMemberId, Long fromMemberId, Pageable pageable);
    Page<Member> findRequestAdminMember(Pageable pageable);
}
