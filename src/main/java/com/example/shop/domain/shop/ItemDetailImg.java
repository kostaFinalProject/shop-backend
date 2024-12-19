package com.example.shop.domain.shop;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemDetailImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_detail_img_id")
    private Long id;

    private String imgName;
    private String originImgName;
    private String imgUrl;

    @Builder
    private ItemDetailImg(String imgName, String originImgName, String imgUrl) {
        this.imgName = imgName;
        this.originImgName = originImgName;
        this.imgUrl = imgUrl;
    }

    public static ItemDetailImg createItemDetailImg(String imgName, String originImgName, String imgUrl) {
        return ItemDetailImg.builder().imgUrl(imgUrl).originImgName(originImgName).build();
    }
}
