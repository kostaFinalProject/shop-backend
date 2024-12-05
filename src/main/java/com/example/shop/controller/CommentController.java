package com.example.shop.controller;

import com.example.shop.dto.instagram.comment.CommentRequestDto;
import com.example.shop.dto.instagram.comment.CommentUpdateRequestDto;
import com.example.shop.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{memberId}")
    public ResponseEntity<?> saveComment(@PathVariable("memberId") Long memberId,
                                         @RequestPart("comment")CommentRequestDto dto,
                                         @RequestPart("file") MultipartFile file) {

        commentService.saveComment(memberId, dto, file);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 생성되었습니다.");
    }

    @PutMapping("/{memberId}/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("memberId") Long memberId,
                                           @PathVariable("commentId") Long commentId,
                                           @RequestPart("comment") CommentUpdateRequestDto dto,
                                           @RequestPart("file") MultipartFile file) {

        commentService.updateComment(memberId, commentId, dto, file);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 수정되었습니다.");
    }

    @PutMapping("/{memberId}/{commentId}/active")
    public ResponseEntity<?> activeComment(@PathVariable("memberId") Long memberId,
                                             @PathVariable("commentId") Long commentId) {

        commentService.activateComment(memberId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 활성화 되었습니다.");
    }

    @PutMapping("/{memberId}/{commentId}/inactive")
    public ResponseEntity<?> inactiveComment(@PathVariable("memberId") Long memberId,
                                             @PathVariable("commentId") Long commentId) {

        commentService.inactivateComment(memberId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 비활성화 되었습니다.");
    }

    @DeleteMapping("/{memberId}/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("memberId") Long memberId,
                                           @PathVariable("commentId") Long commentId) {

        commentService.deleteComment(memberId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 삭제되었습니다.");
    }
}
