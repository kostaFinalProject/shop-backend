package com.example.shop.controller;

import com.example.shop.aop.PublicApi;
import com.example.shop.aop.SecurityAspect;
import com.example.shop.config.CustomUserDetails;
import com.example.shop.dto.qna.AnswerRequestDto;
import com.example.shop.dto.qna.QuestionRequestDto;
import com.example.shop.service.QnAService;
import com.example.shop.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qna")
public class QnAController {

    private final QnAService qnAService;
    private final ValidationService validationService;

    /** 질문 작성 */
    @PostMapping
    public ResponseEntity<?> saveQuestion(@RequestBody QuestionRequestDto dto) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        qnAService.saveQuestion(memberId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("질문 등록에 성공했습니다.");
    }

    /** 질문 수정 */
    @PutMapping("/question/{questionId}")
    public ResponseEntity<?> updateQuestion(@PathVariable("questionId") Long questionId,
                                            @RequestBody QuestionRequestDto dto) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        qnAService.updateQuestion(questionId, memberId ,dto);
        return ResponseEntity.status(HttpStatus.OK).body("질문 수정이 완료되었습니다.");
    }

    /** 질문 삭제 */
    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable("questionId") Long questionId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        qnAService.deleteQuestion(memberId, questionId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 삭제되었습니다.");
    }

    /** 질문 상세페이지 조회 */
    @PublicApi
    @GetMapping("/{questionId}")
    public ResponseEntity<?> getDetailQuestion(@PathVariable Long questionId){
        Long memberId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            if (userDetails.getUsername() != null) {
                String userId = userDetails.getUsername();
                memberId = validationService.validateMemberByUserId(userId).getId();
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(qnAService.getDetailQuestion(memberId, questionId));
    }

    /** 답변 작성 */
    @PostMapping("/answer")
    public ResponseEntity<?> saveAnswer(@RequestBody AnswerRequestDto dto) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        qnAService.saveAnswer(memberId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("답변 등록에 성공했습니다.");
    }

    /** 답변 상세페이지 조회 */
    @PublicApi
    @GetMapping("/answer/{questionId}")
    public ResponseEntity<?> getAnswer(@PathVariable Long questionId) {
        return ResponseEntity.status(HttpStatus.OK).body(qnAService.getAnswerForQuestion(questionId));
    }

    /** 답변 수정 */
    @PutMapping("/answer/{answerId}")
    public ResponseEntity<?> updateAnswer(@PathVariable("answerId") Long answerId,
                                          @RequestBody AnswerRequestDto dto) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        qnAService.updateAnswer(answerId, memberId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("답변 수정이 완료되었습니다.");
    }

    /** 답변 삭제 API */
    @DeleteMapping("/answer/{answerId}")
    public ResponseEntity<String> deleteAnswer(@PathVariable("answerId") Long answerId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        qnAService.deleteAnswer(answerId);
        return ResponseEntity.status(HttpStatus.OK).body("답변이 삭제되었습니다.");
    }

    /** QnA 질문 전체 조회*/
    @PublicApi
    @GetMapping
    public ResponseEntity<?> getAllQuestion(@RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(qnAService.getQnAQuestion(pageable));
    }
}