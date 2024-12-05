package com.example.shop.repository;

import com.example.shop.domain.instagram.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Article> findArticleByMemberId(Long memberId, Pageable pageable);
}
