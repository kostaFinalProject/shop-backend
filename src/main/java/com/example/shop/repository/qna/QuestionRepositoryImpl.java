package com.example.shop.repository.qna;

import com.example.shop.domain.shop.Question;
import com.example.shop.domain.shop.QuestionStatus;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.shop.domain.shop.QQuestion.question;

@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Question> findQuestion(Pageable pageable) {
        List<Question> questions = queryFactory.selectFrom(question)
                .where(question.questionStatus.ne(QuestionStatus.CANCELED))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(question.createAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(question.count())
                .from(question)
                .where(question.questionStatus.ne(QuestionStatus.CANCELED));

        return PageableExecutionUtils.getPage(questions, pageable, countQuery::fetchOne);
    }
}
