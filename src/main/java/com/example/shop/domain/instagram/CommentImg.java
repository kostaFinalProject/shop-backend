package com.example.shop.domain.instagram;

import com.example.shop.domain.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentImg extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_img_id")
    private Long id;

    private String imgName;
    private String originImgName;
    private String imgUrl;

    @Builder
    private CommentImg(String imgName, String originImgName, String imgUrl) {
        this.imgName = imgName;
        this.originImgName = originImgName;
        this.imgUrl = imgUrl;
    }

    public static CommentImg createImg(String imgName, String originImgName, String imgUrl) {
        return new CommentImg(imgName, originImgName, imgUrl);
    }
}