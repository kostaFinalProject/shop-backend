package com.example.shop.domain.instagram;

import com.example.shop.domain.baseentity.BaseEntity;
import com.example.shop.domain.shop.PointGrade;
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
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String userId;
    private String password;
    private String name;

    @Column(unique = true)
    private String nickname;
    private String email;
    private String phone;

    @Embedded
    private Address address;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_profile_img_id")
    private MemberProfileImg memberProfileImg;
    private String introduction;

    private int articles;
    private long followees;
    private long followers;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private PointGrade pointGrade;
    private int points;
    private int payment;

    @Builder
    private Member(String userId, String password, String name, String nickname, String email, String phone,
                   Address address, MemberProfileImg memberProfileImg, String introduction, Grade grade, Provider provider) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.memberProfileImg = memberProfileImg;
        this.introduction = introduction;
        this.grade = grade;
        this.articles = 0;
        this.followees = 0;
        this.followers = 0;
        this.accountStatus = AccountStatus.PRIVATE;
        this.pointGrade = PointGrade.BRONZE;
        this.points = 0;
        this.payment = 0;
        this.provider = provider;
    }

    /** 회원 생성 */
    public static Member createMember(String userId, String password, String name, String nickname, String email,
                                      String phone, Address address, Grade grade, Provider provider) {

        return Member.builder().userId(userId).password(password).name(name).nickname(nickname)
                .email(email).phone(phone).address(address).grade(grade).provider(provider).build();
    }

    /**
     * 게시글 수, 팔로워 수 증감 메서드
     */
    protected void setArticles(int articles) {
        this.articles += articles;
    }

    protected void setFollowees(long followees) {
        this.followees += followees;
    }

    protected void setFollowers(long followers) {
        this.followers += followers;
    }

    /** 관리자 승인 (Super Admin 전용 권한) */
    public void promotionAdmin() {
        this.grade = Grade.ADMIN;
    }

    /** 관리자 권한 해제 (Super Admin 전용 권한) */
    public void relegationAdmin() {
        this.grade = Grade.NO_AUTHORIZATION_ADMIN;
    }

    /** 사용자 게시글, 댓글 사용 권한 중지 (관리자 권한) */
    public void suspendedArticle() {
        this.grade = Grade.SUSPENDED_USER;
    }

    /** 사용자 게시글, 댓글 사용 권한 재부여 (관리자 권한) */
    public void enableArticle() {
        this.grade = Grade.USER;
    }

    /** 회원 정보 수정 */
    public void updateMemberInfo(String userId, String password, String name, String nickname, String phone, Address address) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
    }

    /** Oauth 회원 정보 수정 */
    public void oauthUpdateMemberInfo(String name, String nickname, String phone, Address address) {
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
    }

    /** 프로필 이미지 설정 */
    public void updateMemberProfile(MemberProfileImg memberProfileImg, String introduction) {
        this.memberProfileImg = memberProfileImg;
        this.introduction = introduction;
    }

    public void payment(int price) {
        this.payment += price;

        if (payment >= 300000) {
            this.pointGrade = PointGrade.GOLD;
        } else if (payment >= 100000) {
            this.pointGrade = PointGrade.SILVER;
        }

        this.points += (int)Math.round((double) price / 100.0);
    }

    public void cancelPayment(int price) {
        if (this.payment < price) {
            throw new IllegalStateException("결제 금액보다 큰 금액을 취소할 수 없습니다.");
        }

        this.payment -= price;

        if (payment < 100000) {
            this.pointGrade = PointGrade.BRONZE;
        } else if (payment < 300000) {
            this.pointGrade = PointGrade.SILVER;
        } else {
            this.pointGrade = PointGrade.GOLD;
        }

        int pointsToDeduct = (int) Math.round((double) price / 100.0);
        if (this.points < pointsToDeduct) {
            this.points = 0;
        } else {
            this.points -= pointsToDeduct;
        }
    }


    /** 프로필 공개 여부 설정 */
    public void updateMemberAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public boolean isAccountStatus() {
        return accountStatus == AccountStatus.PUBLIC;
    }

    public void minusPoints(int points) {
        int restPoints = this.points - points;
        if (restPoints < 0) {
            restPoints = 0;
        }
        this.points = restPoints;
    }

    public void plusPoints(int points) {
        this.points += points;
    }
}