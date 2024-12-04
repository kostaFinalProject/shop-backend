package com.example.shop.repository;

import com.example.shop.domain.instagram.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
