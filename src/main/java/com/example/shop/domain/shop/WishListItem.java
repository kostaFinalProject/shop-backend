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
public class WishListItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_list_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    private WishListItem(Member member, Item item) {
        this.member = member;
        this.item = item;
    }

    public static WishListItem createWishListItem(Member member, Item item) {
        return WishListItem.builder().member(member).item(item).build();
    }
}
