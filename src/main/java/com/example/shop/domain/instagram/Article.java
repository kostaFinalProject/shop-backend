package com.example.shop.domain.instagram;

import com.example.shop.domain.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시물
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Article extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleImg> articleImages = new ArrayList<>();

    private String content;

    private long likes;

    private long viewCounts;

    @Enumerated(EnumType.STRING)
    private ArticleStatus articleStatus;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleTag> articleTags = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleItem> articleItems = new ArrayList<>();

    @Builder
    private Article(Member member, List<ArticleImg> articleImages, String content,
                    List<ArticleTag> articleTags, List<ArticleItem> articleItems) {
        this.member = member;
        this.articleImages = articleImages;
        this.content = content;
        this.likes = 0;
        this.viewCounts = 0;
        this.articleStatus = ArticleStatus.ACTIVE;
        this.articleTags = articleTags;
        this.articleItems = articleItems;
    }

    public static Article createArticle(Member member, List<ArticleImg> articleImages, String content,
                                 List<ArticleTag> articleTags, List<ArticleItem> articleItems) {
        Article article = Article.builder().member(member).articleImages(articleImages).content(content)
                .articleTags(articleTags).articleItems(articleItems).build();

        member.setArticles(1);

        for (ArticleImg articleImg : articleImages) {
            articleImg.setArticle(article);
        }

        for (ArticleTag articleTag : articleTags) {
            articleTag.setArticle(article);
        }

        for (ArticleItem articleItem : articleItems) {
            articleItem.setArticle(article);
        }

        return article;
    }

    public void updateArticle(String content) {
        this.content = content;
    }

    /** 게시글 이미지 수정 */
    private void removeArticleImage(ArticleImg articleImg) {
        if (this.articleImages.contains(articleImg)) {
            this.articleImages.remove(articleImg);
            articleImg.setArticle(null);
        }
    }

    public void updateArticleImages(List<ArticleImg> newImages) {
        if (this.articleStatus != ArticleStatus.ACTIVE) {
            throw new IllegalStateException("비활성화 된 게시물이나 삭제된 게시물입니다.");
        }

        for (ArticleImg articleImg : new ArrayList<>(this.articleImages)) {
            if (!newImages.contains(articleImg)) {
                removeArticleImage(articleImg);
            }
        }

        for (ArticleImg articleImg : newImages) {
            if (!this.articleImages.contains(articleImg)) {
                articleImg.setArticle(this);
            }
        }
    }

    /** 게시글 태그 수정 */
    private void removeArticleTag(ArticleTag articleTag) {
        if (this.articleTags.contains(articleTag)) {
            this.articleTags.remove(articleTag);
            articleTag.setArticle(null);
        }
    }

    public void updateArticleTags(List<ArticleTag> newTags) {
        if (this.articleStatus != ArticleStatus.ACTIVE) {
            throw new IllegalStateException("비활성화 된 게시물이나 삭제된 게시물입니다.");
        }

        for (ArticleTag articleTag : new ArrayList<>(this.articleTags)) {
            if (!newTags.contains(articleTag)) {
                removeArticleTag(articleTag);
            }
        }

        for (ArticleTag articleTag : newTags) {
            if (!this.articleTags.contains(articleTag)) {
                articleTag.setArticle(this);
            }
        }
    }

    /** 게시글 상품 태그 수정 */
    private void removeArticleItem(ArticleItem articleItem) {
        if (this.articleItems.contains(articleItem)) {
            this.articleItems.remove(articleItem);
            articleItem.setArticle(null);
        }
    }

    public void updateArticleItems(List<ArticleItem> newItems) {
        if (this.articleStatus != ArticleStatus.ACTIVE) {
            throw new IllegalStateException("비활성화 된 게시물이나 삭제된 게시물입니다.");
        }

        for (ArticleItem articleItem : new ArrayList<>(this.articleItems)) {
            if (!newItems.contains(articleItem)) {
                removeArticleItem(articleItem);
            }
        }

        for (ArticleItem articleItem : newItems) {
            if (!this.articleItems.contains(articleItem)) {
                articleItem.setArticle(this);
            }
        }
    }

    /** 게시글 조회수 증가 */
    public void incrementViewCounts() {
        this.viewCounts++;
    }

    /** 게시글 좋아요 & 취소 */
    protected void setLikes(long likes) {
        this.likes += likes;
    }

    /** 게시글 활성화 */
    public void activeArticle() {
        this.member.setArticles(1);
        this.articleStatus = ArticleStatus.ACTIVE;
    }

    /** 게시글 비활성화 */
    public void inActiveArticle() {
        this.member.setArticles(-1);
        this.articleStatus = ArticleStatus.INACTIVE;
    }

    /** 게시글 삭제 */
    public void deleteArticle() {
        this.member.setArticles(-1);
        this.articleStatus = ArticleStatus.DELETED;
    }
}