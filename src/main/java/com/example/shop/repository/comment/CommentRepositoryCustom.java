package com.example.shop.repository.comment;

import com.example.shop.domain.instagram.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentRepositoryCustom {
    Page<Comment> findReplyCommentsByCommentId(Long commentId, Pageable pageable);
    Optional<Comment> findParentCommentWithArticleByCommentId(Long commentId);
    Optional<Comment> validateCommentAndMemberById(Long commentId, Long memberId);
}
