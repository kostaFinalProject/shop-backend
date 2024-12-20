package com.example.shop.repository.wishlist;

import com.example.shop.domain.shop.ItemStatus;
import com.example.shop.domain.shop.WishListItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.shop.domain.instagram.QMember.member;
import static com.example.shop.domain.shop.QItem.item;
import static com.example.shop.domain.shop.QWishListItem.wishListItem;

@RequiredArgsConstructor
public class WishListItemRepositoryImpl implements WishListItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<WishListItem> findWishListItemByMemberId(Long memberId) {
        return queryFactory.selectFrom(wishListItem)
                .join(wishListItem.member, member).fetchJoin()
                .join(wishListItem.item, item).fetchJoin()
                .where(wishListItem.member.id.eq(memberId)
                        .and(wishListItem.item.itemStatus.eq(ItemStatus.ACTIVE)
                                .or(wishListItem.item.itemStatus.eq(ItemStatus.SOLD_OUT))))
                .fetch();
    }
}
