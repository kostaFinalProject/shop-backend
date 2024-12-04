package com.example.shop.domain.instagram;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder
    private ArticleLike(Member member, Article article) {
        this.member = member;
        this.article = article;
    }

    public static ArticleLike createArticleLike(Member member, Article article) {
        article.setLikes(1);
        return ArticleLike.builder().member(member).article(article).build();
    }

    public void cancelLike() {
        this.article.setLikes(-1);
    }
}
