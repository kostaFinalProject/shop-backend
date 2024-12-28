package com.example.shop.service;

import com.example.shop.domain.instagram.Article;
import com.example.shop.domain.instagram.Comment;
import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.Answer;
import com.example.shop.domain.shop.Item;
import com.example.shop.domain.shop.Question;
import com.example.shop.domain.shop.QuestionStatus;
import com.example.shop.dto.qna.AnswerRequestDto;
import com.example.shop.dto.qna.AnswerResponseDto;
import com.example.shop.dto.qna.QuestionRequestDto;
import com.example.shop.dto.qna.QuestionSummaryResponseDto;
import com.example.shop.repository.qna.AnswerRepository;
import com.example.shop.repository.qna.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnAService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ValidationService validationService;

    /** QnA 게시글 작성 */
    @Transactional
    public void saveQuestion(Long memberId, QuestionRequestDto dto){
        Member member = validationService.validateMemberById(memberId);
        Item item = validationService.validateItemById(dto.getItemId());

        Question question = Question.createQuestion(member, dto.getTitle(), dto.getContent(), item);
        questionRepository.save(question);
    }

    /** 질문 수정 */
    @Transactional
    public void updateQuestion(Long questionId, Long memberId, QuestionRequestDto dto){
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("질문이 없습니다"));

        if (!question.getMember().getId().equals(memberId)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }
        question.updateQuestion(dto.getTitle(), dto.getContent());
    }

    /** 질문 삭제 */
    @Transactional
    public void deleteQuestion(Long memberId, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

        if (!question.getMember().getId().equals(memberId)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }

        question.updateQuestionStatus(QuestionStatus.CANCELED);
    }

    /** QnA 답변 작성 */
    @Transactional
    public void saveAnswer(Long memberId, AnswerRequestDto dto) {
        Member member = validationService.validateMemberById(memberId);

        Question question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 질문을 찾을 수 없습니다."));

        Answer answer = Answer.createAnswer(member, question, dto.getContent());

        answerRepository.save(answer);
    }

    /** QnA 게시물 전체 조회 */
    @Transactional(readOnly = true)
    public Page<QuestionSummaryResponseDto> getQnAQuestion(Pageable pageable) {
        Page<Question> questions=questionRepository.findAll(pageable);

        List<QuestionSummaryResponseDto> dtos = questions.stream()
                .map(question -> {
                    Item item = question.getItem();
                    Member member = question.getMember();

                    return QuestionSummaryResponseDto.readAllDto(
                            question.getId(),
                            member.getId(),
                            item.getRepItemImage(),
                            question.getTitle(),
                            question.getCreateAt(),
                            question.getQuestionStatus().name()
                    );
                })
                .toList();

        return new PageImpl<>(dtos, pageable, questions.getTotalElements());
    }

    /** 질문 QnA 상세페이지 조회 */
    @Transactional(readOnly = true)
    public QuestionSummaryResponseDto getDetailQuestion(Long memberId, Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문을 찾을 수 없습니다."));

        Member member = null;
        if (memberId != null) {
            member = validationService.validateMemberById(memberId);
        }

        String isMe = "Not Me";
        if (question.getMember().equals(member)) {
            isMe = "Me";
        }

        return QuestionSummaryResponseDto.readDetailDto(
                question.getId(),
                question.getMember().getId(),
                question.getItem().getRepItemImage(),
                question.getItem().getName(),
                question.getItem().getPrice(),
                question.getTitle(),
                isMe,
                question.getCreateAt(),
                question.getQuestionStatus().name(),
                question.getContent()
        );
    }

    /** 답변 QnA 상세페이지 조회 */
    @Transactional(readOnly = true)
    public List<AnswerResponseDto> getAnswerForQuestion(Long questionId) {
        List<Answer> answers = answerRepository.findByQuestionId(questionId);

        return answers.stream()
                .map(answer -> AnswerResponseDto.readAnswerDto(answer.getId(), questionId, answer.getMember().getId(),
                        answer.getCreateAt(), answer.getContent()))
                .toList();
    }

    /** QnA 답변 수정 */
    @Transactional
    public void updateAnswer(Long answerId, Long memberId, AnswerRequestDto dto) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변이 존재하지 않습니다."));

        if (!answer.getMember().getId().equals(memberId)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }

        answer.updateAnswer(dto.getContent());
        answer.getQuestion().updateQuestionStatus(QuestionStatus.COMPLETED);
    }

    /** 댓글 삭제 */
    @Transactional
    public void deleteAnswer(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변이 없습니다."));

        answer.getQuestion().updateQuestionStatus(QuestionStatus.PROGRESS);

        answerRepository.delete(answer);
    }
}
