package com.example.shop.domain.instagram;

import com.example.shop.domain.shop.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 해시태그
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    private ArticleItem(Item item) {
        this.item = item;
    }

    public static ArticleItem createArticleItem(Item item) {
        return ArticleItem.builder().item(item).build();
    }

    protected void setArticle(Article article) {
        if (this.article != null) {
            this.article.getArticleItems().remove(this);
        }

        this.article = article;
        if (this.article != null && !this.article.getArticleItems().contains(this)) {
            this.article.getArticleItems().add(this);
        }
    }
}
