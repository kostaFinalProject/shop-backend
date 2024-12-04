package com.example.shop.domain.instagram;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 내 게시물 저장
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleCollection {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_collection_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder
    private ArticleCollection(Member member, Article article) {
        this.member = member;
        this.article = article;
    }

    public static ArticleCollection createArticleCollection(Member member, Article article) {
        return ArticleCollection.builder().member(member).article(article).build();
    }
}
