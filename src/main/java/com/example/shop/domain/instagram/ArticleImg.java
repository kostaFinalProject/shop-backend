package com.example.shop.domain.instagram;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시물 이미지
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    private String imgName;
    private String originImgName;
    private String imgUrl;

    @Builder
    private ArticleImg(String imgName, String originImgName, String imgUrl) {
        this.imgName = imgName;
        this.originImgName = originImgName;
        this.imgUrl = imgUrl;
    }

    public static ArticleImg createArticleImg(String imgName, String originImgName, String imgUrl) {
        return ArticleImg.builder().imgName(imgName)
                .originImgName(originImgName).imgUrl(imgUrl).build();
    }

    /** 연관 관계 메서드 */
    protected void setArticle(Article article) {
        if (this.article != null) {
            this.article.getArticleImages().remove(this);
        }

        this.article = article;
        if (article != null && !article.getArticleImages().contains(this)) {
            article.getArticleImages().add(this);
        }
    }
}
