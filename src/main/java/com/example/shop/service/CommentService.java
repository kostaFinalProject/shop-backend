package com.example.shop.service;

import com.example.shop.domain.instagram.*;
import com.example.shop.dto.instagram.comment.CommentRequestDto;
import com.example.shop.dto.instagram.comment.CommentUpdateRequestDto;
import com.example.shop.repository.CommentRepository;
import com.example.shop.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ImageService imageService;
    private final ValidationService validationService;

    @Transactional
    public void saveComment(Long memberId, CommentRequestDto dto, MultipartFile file) {
        Member member = validationService.validateMemberById(memberId);
        Article article = validationService.validateArticleById(dto.getArticleId());

        Comment parentComment = null;
        if (dto.getParentCommentId() != null) {
            parentComment = validationService.validateCommentById(dto.getParentCommentId());
        }

        CommentImg commentImg = null;
        if (file != null && !file.isEmpty()) {
            commentImg = imageService.saveCommentImg(file);
        }

        Comment comment = Comment.createComment(article, member, dto.getContent(), parentComment, commentImg);

        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long memberId, Long commentId, CommentUpdateRequestDto dto, MultipartFile file) {
        Member member = validationService.validateMemberById(memberId);
        Comment comment = validationService.validateCommentById(commentId);

        if (!comment.getMember().equals(member)) {
            throw new IllegalArgumentException("수정할 권한이 없습니다.");
        }

        CommentImg commentImg = null;
        if (file != null && !file.isEmpty()) {
            commentImg = imageService.updateCommentImg(comment, file);
        } else {
            if (comment.getCommentImg() != null) {
                imageService.deleteCommentImg(comment);
            }
        }

        comment.updateComment(dto.getContent(), commentImg);
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        Member member = validationService.validateMemberById(memberId);
        Comment comment = validationService.validateCommentById(commentId);

        if (member.getGrade() == Grade.USER && !comment.getMember().equals(member)) {
            throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
        }

        imageService.deleteCommentImg(comment);
        comment.deleteComment();
    }
}

