package com.example.shop.domain.shop;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Discount {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int discountPercent;
    private int discountPrice; // 추가된 필드

    @Builder
    private Discount(Item item, int discountPercent, int discountPrice) {
        this.item = item;
        this.discountPercent = discountPercent;
        this.discountPrice = discountPrice;
    }

    public static Discount createDiscount(Item item, int discountPercent) {
        int discountPrice = calculateDiscountPrice(item.getPrice(), discountPercent);
        return Discount.builder().item(item).discountPercent(discountPercent).discountPrice(discountPrice).build();
    }

    public void updateDiscount(int discountPercent) {
        this.discountPercent = discountPercent;
        this.discountPrice = calculateDiscountPrice(this.item.getPrice(), discountPercent);
    }

    private static int calculateDiscountPrice(int originalPrice, int discountPercent) {
        if (discountPercent < 1 || discountPercent > 100) {
            throw new IllegalArgumentException("할인 비율은 0% ~ 100% 사이여야 합니다.");
        }

        double discountMultiplier = 1 - (discountPercent / 100.0);
        return (int) Math.round(originalPrice * discountMultiplier);
    }
}
