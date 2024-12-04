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
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 성공적으로 생성되었습니다.");
    }

    @PutMapping("/{memberId}/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("memberId") Long memberId,
                                           @PathVariable("commentId") Long commentId,
                                           @RequestPart("comment") CommentUpdateRequestDto dto,
                                           @RequestPart("file") MultipartFile file) {

        commentService.updateComment(memberId, commentId, dto, file);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 성공적으로 수정되었습니다.");
    }
}
