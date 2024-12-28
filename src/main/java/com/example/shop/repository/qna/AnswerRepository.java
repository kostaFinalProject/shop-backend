package com.example.shop.repository.qna;

import com.example.shop.domain.shop.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerRepositoryCustom {
}
