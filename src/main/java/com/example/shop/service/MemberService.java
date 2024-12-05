package com.example.shop.service;

import com.example.shop.domain.instagram.Article;
import com.example.shop.domain.instagram.Member;
import com.example.shop.dto.MemberFormDto;
import com.example.shop.dto.instagram.article.ArticleSummaryResponseDto;
import com.example.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void saveMember(MemberFormDto dto) {
        Member member = Member.createMember(dto.getName());
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Page<ArticleSummaryResponseDto> getArticle(Long memberId, Pageable pageable) {

        Page<Article> articles = memberRepository.findArticleByMemberId(memberId, pageable);

        List<ArticleSummaryResponseDto> dtos = articles.stream()
                .map(article -> {
                    ArticleSummaryResponseDto dto = new ArticleSummaryResponseDto();

                    dto.setMemberId(memberId);
                    dto.setMemberName(article.getMember().getName());
                    dto.setImageUrl(article.getArticleImages().get(0).getImgUrl());
                    dto.setContent(article.getContent());
                    dto.setLikeCount(article.getLikes());

                    return dto;
                })
                .toList();

        return new PageImpl<>(dtos, pageable, articles.getTotalElements());
    }
}
