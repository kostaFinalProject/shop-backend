package com.example.shop.repository.qna;

import com.example.shop.domain.shop.Answer;
import com.example.shop.domain.shop.Question;

import java.util.List;
import java.util.Optional;

public interface AnswerRepositoryCustom {
    List<Answer> findByQuestionId(Long questionId);
}
