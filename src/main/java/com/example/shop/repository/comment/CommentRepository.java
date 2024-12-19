package com.example.shop.repository.comment;

import com.example.shop.domain.instagram.Comment;
import com.example.shop.domain.instagram.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    long countByArticleIdAndCommentStatus(Long articleId, CommentStatus status);
}
