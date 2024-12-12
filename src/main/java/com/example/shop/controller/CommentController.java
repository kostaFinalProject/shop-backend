package com.example.shop.controller;

import com.example.shop.aop.PublicApi;
import com.example.shop.aop.SecurityAspect;
import com.example.shop.config.CustomUserDetails;
import com.example.shop.dto.instagram.comment.CommentRequestDto;
import com.example.shop.dto.instagram.comment.CommentUpdateRequestDto;
import com.example.shop.service.CommentService;
import com.example.shop.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Spring Security 적용하면 @PathVariable("memberId") 제거
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;
    private final ValidationService validationService;

    /** 대댓글 작성 */
    @PostMapping("/{memberId}/{commentId}")
    public ResponseEntity<?> saveReply(@PathVariable("memberId") Long memberId,
                                         @PathVariable("commentId") Long commentId,
                                         @RequestPart("comment")CommentRequestDto dto,
                                         @RequestPart("file") MultipartFile file) {

        commentService.saveReply(memberId, commentId, dto, file);
        return ResponseEntity.status(HttpStatus.OK).body("대댓글이 생성되었습니다.");
    }

    /** 대댓글 전체 조회 (페이징 적용) */
    @PublicApi
    @GetMapping("/{commentId}")
    public ResponseEntity<?> getReplies(@PathVariable("commentId") Long commentId,
                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "5") int size) {

        Long memberId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            if (userDetails.getUsername() != null) {
                String userId = userDetails.getUsername();
                memberId = validationService.validateMemberByUserId(userId).getId();
            }
        }

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getReplies(memberId, commentId, pageable));
    }

    /** 댓글 및 대댓글 수정 (작성자 or 관리자만 가능) */
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId,
                                           @RequestPart("comment") CommentUpdateRequestDto dto,
                                           @RequestPart("file") MultipartFile file) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        commentService.updateComment(memberId, commentId, dto, file);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 수정되었습니다.");
    }

    /** 댓글 및 대댓글 활성화 (관리자 기능) */
    @PutMapping("/active/{commentId}")
    public ResponseEntity<?> activeComment(@PathVariable("commentId") Long commentId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        commentService.activateComment(memberId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 활성화 되었습니다.");
    }

    /** 댓글 및 대댓글 비활성화 (관리자 기능) */
    @PutMapping("/inactive/{commentId}")
    public ResponseEntity<?> inactiveComment(@PathVariable("commentId") Long commentId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        commentService.inactivateComment(memberId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 비활성화 되었습니다.");
    }

    /** 댓글 및 대댓글 삭제 (작성자 or 관리자만 가능) */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        commentService.deleteComment(memberId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 삭제되었습니다.");
    }
}
