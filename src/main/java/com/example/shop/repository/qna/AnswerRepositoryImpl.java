package com.example.shop.repository.qna;

import com.example.shop.domain.shop.Answer;
import com.example.shop.domain.shop.Question;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.shop.domain.shop.QAnswer.answer;
import static com.example.shop.domain.shop.QQuestion.question;

@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Answer> findByQuestionId(Long questionId) {
        return queryFactory.selectFrom(answer)
                .join(answer.question, question)
                .where(answer.question.id.eq(questionId))
                .fetch();
    }
}
