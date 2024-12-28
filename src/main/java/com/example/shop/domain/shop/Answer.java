package com.example.shop.domain.shop;

import com.example.shop.domain.baseentity.BaseEntity;
import com.example.shop.domain.instagram.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @Builder
    private Answer(Member member, Question question,
                   String content) {
        this.member = member;
        this.question=question;
        this.content = content;
    }

    public static Answer createAnswer(Member member,
                                      Question question,
                                      String content){
        question.updateQuestionStatus(QuestionStatus.COMPLETED);
        return Answer.builder()
                .member(member)
                .question(question)
                .content(content)
                .build();
    }

    public void updateAnswer(String content){
        this.content = content;
    }
}
