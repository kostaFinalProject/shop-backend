package com.example.shop.domain.instagram;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private int articles;
    private long followees; // 사용자를 팔로우 하는 사람의 수
    private long followers; // 사용자가 팔로우 하는 사람의 수

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Builder
    private Member(String name) {
        this.name = name;
        this.articles = 0;
        this.followees = 0;
        this.followers = 0;
        this.grade = Grade.USER;
    }

    public static Member createMember(String name) {
        return Member.builder().name(name).build();
    }

    protected void setArticles(int articles) {
        this.articles += articles;
    }

    protected void setFollowees(long followees) {
        this.followees += followees;
    }

    protected void setFollowers(long followers) {
        this.followers += followers;
    }
}
