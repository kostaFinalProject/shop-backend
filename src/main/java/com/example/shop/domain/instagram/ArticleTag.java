package com.example.shop.domain.instagram;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시물 자유 해시태그
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Builder
    private ArticleTag(Tag tag) {
        this.tag = tag;
    }

    public static ArticleTag createArticleTag(Tag tag) {
        return ArticleTag.builder().tag(tag).build();
    }

    /** 연관관계 메서드 */
    protected void setArticle(Article article) {
        if (this.article != null) {
            this.article.getArticleTags().remove(this);
        }

        this.article = article;
        if (article != null && !this.article.getArticleTags().contains(this)) {
            this.article.getArticleTags().add(this);
        }
    }
}
