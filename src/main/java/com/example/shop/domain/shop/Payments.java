package com.example.shop.domain.shop;

import com.example.shop.domain.baseentity.BaseEntity;
import com.example.shop.domain.instagram.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payments extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int paymentPrice;

    private int usePoints;

    @Column(name = "imp_uid", unique = true, nullable = false)
    private String impUid;

    @Builder
    private Payments(Member member, Order order, int paymentPrice, int usePoints, String impUid) {
        this.member = member;
        this.order = order;
        this.paymentPrice = paymentPrice;
        this.usePoints = usePoints;
        this.impUid = impUid;
    }

    public static Payments createPayment(Member member, Order order, int paymentPrice, int usePoints, String impUid) {
        return Payments.builder()
                .member(member)
                .order(order)
                .paymentPrice(paymentPrice)
                .usePoints(usePoints)
                .impUid(impUid)
                .build();
    }
}