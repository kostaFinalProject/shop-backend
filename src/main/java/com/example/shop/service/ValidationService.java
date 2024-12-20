package com.example.shop.service;

import com.example.shop.domain.instagram.*;
import com.example.shop.domain.shop.*;
import com.example.shop.repository.*;
import com.example.shop.repository.article.ArticleRepository;
import com.example.shop.repository.articlelike.ArticleLikeRepository;
import com.example.shop.repository.block.BlockRepository;
import com.example.shop.repository.cart.CartRepository;
import com.example.shop.repository.comment.CommentRepository;
import com.example.shop.repository.commentlike.CommentLikeRepository;
import com.example.shop.repository.discount.DiscountRepository;
import com.example.shop.repository.follower.FollowerRepository;
import com.example.shop.repository.item.ItemRepository;
import com.example.shop.repository.itemcategory.ItemCategoryRepository;
import com.example.shop.repository.member.MemberRepository;
import com.example.shop.repository.wishlist.WishListItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    private final BlockRepository blockRepository;
    private final DiscountRepository discountRepository;
    private final CartRepository cartRepository;
    private final WishListItemRepository wishListItemRepository;

    public Member validateMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유효한 회원이 아닙니다."));
    }

    public Member validateMemberByUserId(String userId) {
        return memberRepository.findByUserId(userId)
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

    public Item findItemById(Long itemId) {
        return itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 상품이 없습니다."));
    }

    public Discount validateDiscountById(Long discountId) {
        return discountRepository.findById(discountId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 할인이 없습니다."));
    }

    public Discount findDiscountByItemId(Long itemId) {
        return discountRepository.findDiscountByItemId(itemId)
                .orElse(null);
    }

    public Cart validateCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 장바구니 상품이 없습니다."));
    }

    public WishListItem validateWishListItemById(Long wishListItemId) {
        return wishListItemRepository.findById(wishListItemId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 관심 상품이 없습니다."));
    }

    public boolean existItemNameInItemCategory(String category, String itemName) {
        return itemRepository.existsNameInCategory(category, itemName);
    }

    public boolean existItemNameInItemCategoryExceptSelf(String category, String itemName, Long itemId) {
        return itemRepository.existsNameInCategoryExceptMe(category, itemName, itemId);
    }

    public Size validateSizeBySize(String size) {
        return sizeRepository.findBySize(size)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사이즈입니다."));
    }

    public Tag validateTagByName(String tagName) {
        return tagRepository.findByTag(tagName)
                .map(tag -> {
                    tag.incrementCount();
                    return tag;
                })
                .orElseGet(() -> tagRepository.save(Tag.createTag(tagName)));
    }

    public Follower validateFollowerById(Long followId) {
        return followerRepository.findFollowerWithFolloweeAndFollowerById(followId)
                .orElseThrow(() -> new IllegalArgumentException("유효한 팔로우 관계가 아닙니다."));
    }

    public boolean validateArticleCollectionByArticleAndMember(Member member, Article article) {
        return articleCollectionRepository.existsByMemberAndArticle(member, article);
    }

    public ArticleCollection validateArticleCollectionById(Long articleCollectionId) {
        return articleCollectionRepository.findById(articleCollectionId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 저장된 게시물 입니다."));
    }

    public Article validateArticleAndMemberById(Long articleId, Long memberId) {
        return articleRepository.validateArticleAndMemberById(articleId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 게시글이 없거나 게시글을 작성한 사용자가 아닙니다."));
    }

    public Comment validateCommentAndMemberById(Long commentId, Long memberId) {
        return commentRepository.validateCommentAndMemberById(commentId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 없거나 댓글을 작성한 사용자가 아닙니다."));
    }

    public boolean existArticleLikeByArticleIdAndMemberId(Long articleId, Long memberId) {
        return articleLikeRepository.existsByArticleAndMemberById(articleId, memberId);
    }

    public boolean existCommentLikeByCommentIdAndMemberId(Long commentId, Long memberId) {
        return commentLikeRepository.existsByCommentAndMember(commentId, memberId);
    }

    public Long findArticleLikeIdByArticleAndMember(Long articleId, Long memberId) {
        return articleLikeRepository.findArticleLikeIdByArticleAndMember(articleId, memberId);
    }

    public Long findCommentLikeIdByCommentAndMember(Long commentId, Long memberId) {
        return commentLikeRepository.findCommentLikeIdByCommentAndMember(commentId, memberId);
    }

    public ArticleLike findArticleLikeById(Long articleLikeId) {
        return articleLikeRepository.findArticleLikeById(articleLikeId)
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누르지 않은 게시글입니다."));
    }

    public CommentLike findCommentLikeById(Long commentLikeId) {
        return commentLikeRepository.findCommentLikeById(commentLikeId)
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누르지 않은 댓글입니다."));
    }

    public boolean existByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId) {
        return blockRepository.existByFromMemberIdAndToMemberId(fromMemberId, toMemberId);
    }

    public Block findBlockWithFromMemberAndToMemberById(Long blockId) {
        return blockRepository.findBlockWithFromMemberAndToMemberById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 차단 정보입니다."));
    }

    public Follower findFollowerByFolloweeIdAndFollowerId(Long followeeId, Long followerId) {
        return followerRepository.findByFolloweeIdAndFollowerId(followeeId, followerId);
    }
}