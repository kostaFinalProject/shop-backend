package com.example.shop.service;

import com.example.shop.domain.instagram.*;
import com.example.shop.domain.shop.Item;
import com.example.shop.domain.shop.ItemCategory;
import com.example.shop.domain.shop.Size;
import com.example.shop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 각종 엔티티 검증 서비스
 */
@Service
@RequiredArgsConstructor
public class ValidationService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemRepository itemRepository;
    private final SizeRepository sizeRepository;
    private final TagRepository tagRepository;
    private final FollowerRepository followerRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ArticleCollectionRepository articleCollectionRepository;

    public Member validateMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유효한 회원이 아닙니다."));
    }

    public Article validateArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 게시글이 아닙니다."));
    }

    public Comment validateCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 아닙니다."));
    }

    public ItemCategory validateItemCategoryByName(String categoryName) {
        return itemCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("등록된 카테고리가 없습니다."));
    }

    public ItemCategory validateItemCategoryById(Long categoryId) {
        return itemCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 카테고리가 없습니다."));
    }

    public Item validateItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 상품이 없습니다."));
    }

    public boolean existItemName(String itemName) {
        return itemRepository.existsByName(itemName);
    }

    public boolean existItemNameExceptSelf(String itemName, Long itemId) {
        return itemRepository.existsByNameAndIdNot(itemName, itemId);
    }

    public Size validateSizeBySize(String size) {
        return sizeRepository.findBySize(size)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사이즈입니다."));
    }

    public Tag validateTagByName(String tagName) {
        return tagRepository.findByTag(tagName)
                .orElseGet(() -> tagRepository.save(Tag.createTag(tagName)));
    }

    public Follower validateFollowerByFolloweeAndFollower(Member followeeMember, Member followerMember) {
        return followerRepository.findByFolloweeAndFollower(followeeMember, followerMember)
                .orElseThrow(() -> new IllegalArgumentException("유효한 팔로우 관계가 아닙니다."));
    }

    public Optional<ArticleLike> validateArticleLikeByArticleAndMember(Article article, Member member) {
        return articleLikeRepository.findByArticleAndMember(article, member);
    }

    public Optional<CommentLike> validateCommentLikeByCommentAndMember(Comment comment, Member member) {
        return commentLikeRepository.findByCommentAndMember(comment, member);
    }

    public boolean validateArticleCollectionByArticleAndMember(Member member, Article article) {
        return articleCollectionRepository.existsByMemberAndArticle(member, article);
    }

    public Article validationArticleAndMemberById(Long articleId, Long memberId) {
        return articleRepository.validationArticleAndMemberById(articleId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 게시글이 없거나 게시글을 작성한 사용자가 아닙니다."));
    }

    public Comment validationCommentAndMemberById(Long commentId, Long memberId) {
        return commentRepository.validationCommentAndMemberById(commentId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 없거나 댓글을 작성한 사용자가 아닙니다."));
    }
}