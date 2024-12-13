package com.example.shop.repository.commentlike;

import com.example.shop.domain.instagram.CommentLike;

import java.util.Optional;

public interface CommentLikeRepositoryCustom {
    boolean existsByCommentAndMember(Long commentId, Long memberId);
    Long findCommentLikeIdByCommentAndMember(Long commentId, Long memberId);
    Optional<CommentLike> findCommentLikeById(Long commentLikeId);
}
