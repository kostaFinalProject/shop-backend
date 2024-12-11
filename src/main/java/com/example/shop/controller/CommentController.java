package com.example.shop.controller;

import com.example.shop.aop.PublicApi;
import com.example.shop.dto.instagram.comment.CommentRequestDto;
import com.example.shop.dto.instagram.comment.CommentUpdateRequestDto;
import com.example.shop.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/{memberId}/{commentId}")
    public ResponseEntity<?> getReplies(@PathVariable("memberId") Long memberId,
                                        @PathVariable("commentId") Long commentId,
                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getReplies(memberId, commentId, pageable));
    }

    /** 댓글 및 대댓글 수정 (작성자 or 관리자만 가능) */
    @PutMapping("/{memberId}/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("memberId") Long memberId,
                                           @PathVariable("commentId") Long commentId,
                                           @RequestPart("comment") CommentUpdateRequestDto dto,
                                           @RequestPart("file") MultipartFile file) {

        commentService.updateComment(memberId, commentId, dto, file);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 수정되었습니다.");
    }

    /** 댓글 및 대댓글 활성화 (관리자 기능) */
    @PutMapping("/{memberId}/{commentId}/active")
    public ResponseEntity<?> activeComment(@PathVariable("memberId") Long memberId,
                                             @PathVariable("commentId") Long commentId) {

        commentService.activateComment(memberId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 활성화 되었습니다.");
    }

    /** 댓글 및 대댓글 비활성화 (관리자 기능) */
    @PutMapping("/{memberId}/{commentId}/inactive")
    public ResponseEntity<?> inactiveComment(@PathVariable("memberId") Long memberId,
                                             @PathVariable("commentId") Long commentId) {

        commentService.inactivateComment(memberId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 비활성화 되었습니다.");
    }

    /** 댓글 및 대댓글 삭제 (작성자 or 관리자만 가능) */
    @DeleteMapping("/{memberId}/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("memberId") Long memberId,
                                           @PathVariable("commentId") Long commentId) {

        commentService.deleteComment(memberId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 삭제되었습니다.");
    }
}
