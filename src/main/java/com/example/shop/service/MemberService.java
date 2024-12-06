package com.example.shop.service;

import com.example.shop.domain.instagram.Article;
import com.example.shop.domain.instagram.ArticleCollection;
import com.example.shop.domain.instagram.Follower;
import com.example.shop.domain.instagram.Member;
import com.example.shop.dto.MemberFormDto;
import com.example.shop.dto.member.ArticleCollectionResponseDto;
import com.example.shop.dto.instagram.article.ArticleSummaryResponseDto;
import com.example.shop.dto.member.FollowerListResponseDto;
import com.example.shop.repository.member.MemberRepository;
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
    private final ValidationService validationService;

    /** 회원 가입 */
    @Transactional
    public void saveMember(MemberFormDto dto) {
        Member member = Member.createMember(dto.getName());
        memberRepository.save(member);
    }

    /** 회원의 게시물 조회 */
    @Transactional(readOnly = true)
    public Page<ArticleSummaryResponseDto> getArticle(Long memberId, Pageable pageable) {

        Page<Article> articles = memberRepository.findArticleByMemberId(memberId, pageable);

        List<ArticleSummaryResponseDto> dtos = articles.stream()
                .map(article -> {
                    boolean liked = validationService.existArticleLikeByArticleIdAndMemberId(article.getId(), memberId);
                    Long likeId = validationService.findArticleLikeIdByArticleAndMember(article.getId(), memberId);

                    return ArticleSummaryResponseDto.createDto(article.getId(), memberId, article.getMember().getName(),
                            article.getArticleImages().get(0).getImgUrl(),
                            article.getContent(), article.getLikes(), article.getViewCounts(), liked, likeId);
                })
                .toList();

        return new PageImpl<>(dtos, pageable, articles.getTotalElements());
    }

    /** 회원의 저장된 게시물 조회 */
    @Transactional(readOnly = true)
    public Page<ArticleCollectionResponseDto> getArticleCollection(Long memberId, Pageable pageable) {

        Page<ArticleCollection> articleCollections = memberRepository.findArticleCollectionByMemberId(memberId, pageable);

        List<ArticleCollectionResponseDto> dtos = articleCollections.stream()
                .map(articleCollection -> {
                    boolean liked = validationService.existArticleLikeByArticleIdAndMemberId(articleCollection.getArticle().getId(), memberId);
                    Long likeId = validationService.findArticleLikeIdByArticleAndMember(articleCollection.getArticle().getId(), memberId);

                    return ArticleCollectionResponseDto.createDto(articleCollection.getId(),
                            articleCollection.getArticle().getId(), articleCollection.getMember().getId(),
                            articleCollection.getMember().getName(), articleCollection.getArticle().getArticleImages().get(0).getImgUrl(),
                            articleCollection.getArticle().getContent(), articleCollection.getArticle().getLikes(),
                            articleCollection.getArticle().getViewCounts(), liked, likeId);
                })
                .toList();

        return new PageImpl<>(dtos, pageable, articleCollections.getTotalElements());
    }

    /** 회원의 팔로우 리스트 조회 */
    public Page<FollowerListResponseDto> getFollower(Long memberId, Pageable pageable) {

        Page<Follower> followers = memberRepository.findFollowerByMemberId(memberId, pageable);

        List<FollowerListResponseDto> dtos = followers.stream()
                .map(follower -> FollowerListResponseDto.createDto(follower.getId(),
                        follower.getFollower().getId(), follower.getFollower().getName()))
                .toList();

        return new PageImpl<>(dtos, pageable, followers.getTotalElements());
    }
}
