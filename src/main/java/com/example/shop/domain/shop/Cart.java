package com.example.shop.domain.shop;

import com.example.shop.domain.instagram.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_size_id")
    private ItemSize itemSize;

    private int quantity;

    @Builder
    private Cart (Member member, ItemSize itemSize, int quantity) {
        this.member = member;
        this.itemSize = itemSize;
        this.quantity = quantity;
    }

    public static Cart createCart(Member member, ItemSize itemSize, int quantity) {
        return Cart.builder().member(member).itemSize(itemSize).quantity(quantity).build();
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
