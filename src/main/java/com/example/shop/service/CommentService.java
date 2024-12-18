package com.example.shop.service;

import com.example.shop.domain.instagram.*;
import com.example.shop.dto.instagram.comment.CommentRequestDto;
import com.example.shop.dto.instagram.comment.CommentUpdateRequestDto;
import com.example.shop.dto.instagram.comment.ReplyCommentResponseDto;
import com.example.shop.repository.comment.CommentRepository;
import com.example.shop.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ImageService imageService;
    private final ValidationService validationService;

    @Transactional
    public void saveReply(Long memberId, Long commentId, CommentRequestDto dto, MultipartFile file) {
        Member member = validationService.validateMemberById(memberId);
        Comment parentComment = commentRepository.findParentCommentWithArticleByCommentId(commentId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 댓글이 아닙니다."));

        CommentImg commentImg = null;
        if (file != null && !file.isEmpty()) {
            commentImg = imageService.saveCommentImg(file);
        } else {
            if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("댓글에는 글 또는 이미지 중 하나가 반드시 포함되어야 합니다.");
            }
        }

        Comment comment = Comment.createComment(parentComment.getArticle(), member, dto.getContent(), parentComment, commentImg);

        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long memberId, Long commentId, CommentUpdateRequestDto dto, MultipartFile file) {
        Comment comment = validationService.validateCommentAndMemberById(commentId, memberId);

        if (!comment.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("수정할 권한이 없습니다.");
        }

        CommentImg commentImg = null;
        if (file != null && !file.isEmpty()) {
            commentImg = imageService.updateCommentImg(comment, file);
        } else {
            if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("댓글에는 글 또는 이미지 중 하나가 반드시 포함되어야 합니다.");
            }

            if (comment.getCommentImg() != null) {
                imageService.deleteCommentImg(comment);
            }
        }

        comment.updateComment(dto.getContent(), commentImg);
    }

    @Transactional(readOnly = true)
    public Page<ReplyCommentResponseDto> getReplies(Long memberId, Long commentId, Pageable pageable) {
        Page<Comment> replies = commentRepository.findReplyCommentsByCommentId(memberId, commentId, pageable);

        List<ReplyCommentResponseDto> dtos = replies.stream()
                .map(reply -> {
                    Long likeId = validationService.findCommentLikeIdByCommentAndMember(reply.getId(), memberId);
                    String isMe = "Not Me";
                    if (reply.getMember().getId().equals(memberId)) {
                        isMe = "Me";
                    }

                    String memberProfileImageUrl = null;
                    if (reply.getMember().getMemberProfileImg() != null) {
                        memberProfileImageUrl = reply.getMember().getMemberProfileImg().getImgUrl();
                    }
                    LocalDateTime time = reply.getCreateAt();
                    if (reply.getUpdateAt() != null) {
                        time = reply.getUpdateAt();
                    }

                    return ReplyCommentResponseDto.createDto(reply.getId(), memberProfileImageUrl,
                            reply.getMember().getNickname(), reply.getContent(),
                            reply.getCommentImg().getImgUrl(), reply.getLikes(), likeId, isMe, time);
                })
                .toList();

        return new PageImpl<>(dtos, pageable, replies.getTotalElements());
    }

    @Transactional
    public void activateComment(Long memberId, Long commentId) {
        Member member = validationService.validateMemberById(memberId);
        Comment comment = validationService.validateCommentById(commentId);

        if (member.getGrade() == Grade.USER) {
            throw new IllegalArgumentException("댓글을 비활성화할 권한이 없습니다.");
        }

        comment.activeComment();
    }

    @Transactional
    public void inactivateComment(Long memberId, Long commentId) {
        Member member = validationService.validateMemberById(memberId);
        Comment comment = validationService.validateCommentById(commentId);

        if (member.getGrade() == Grade.USER) {
            throw new IllegalArgumentException("댓글을 비활성화할 권한이 없습니다.");
        }

        comment.inactiveComment();
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