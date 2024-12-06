package com.example.shop.repository.member;

import com.example.shop.domain.instagram.Article;
import com.example.shop.domain.instagram.ArticleCollection;
import com.example.shop.domain.instagram.Block;
import com.example.shop.domain.instagram.Follower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Article> findArticleByMemberId(Long memberId, Pageable pageable);
    Page<ArticleCollection> findArticleCollectionByMemberId(Long memberId, Pageable pageable);
    Page<Follower> findFollowerByMemberId(Long memberId, Pageable pageable);
    Page<Block> findBlockByMemberId(Long memberId, Pageable pageable);
}
