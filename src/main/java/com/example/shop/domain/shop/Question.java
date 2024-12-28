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
public class Question extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    private Question(Member member, String title,
                     String content, Item item) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.questionStatus = QuestionStatus.PROGRESS;
        this.item = item;
    }

    public static Question createQuestion(Member member, String title,
                                          String content, Item item){
        return Question.builder().member(member)
                .title(title).content(content).item(item).build();
    }

    public void updateQuestion(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void updateQuestionStatus (QuestionStatus questionStatus){
        this.questionStatus = questionStatus;
    }
}
