package com.example.shop.repository.qna;

import com.example.shop.domain.shop.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionRepositoryCustom {
    Page<Question> findQuestion(Pageable pageable);
}
