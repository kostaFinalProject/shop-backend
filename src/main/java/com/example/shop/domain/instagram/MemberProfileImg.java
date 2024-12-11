package com.example.shop.domain.instagram;

import com.example.shop.domain.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 프로필 이미지
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberProfileImg extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_profile_img_id")
    private Long id;

    private String imgName;
    private String originImgName;
    private String imgUrl;

    @Builder
    private MemberProfileImg(String imgName, String originImgName, String imgUrl) {
        this.imgName = imgName;
        this.originImgName = originImgName;
        this.imgUrl = imgUrl;
    }

    public static MemberProfileImg createMemberProfileImg(String imgName, String originImgName, String imgUrl) {
        return MemberProfileImg.builder().imgName(imgName).originImgName(originImgName).imgUrl(imgUrl).build();
    }
}
