package com.example.shop.repository.cart;

import com.example.shop.domain.shop.Cart;
import com.example.shop.domain.shop.ItemStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.shop.domain.instagram.QMember.member;
import static com.example.shop.domain.shop.QCart.cart;
import static com.example.shop.domain.shop.QItem.item;
import static com.example.shop.domain.shop.QItemSize.itemSize;

@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Cart> findCartDetails(Long memberId, Long itemSizeId) {
        return queryFactory.selectFrom(cart)
                .join(cart.member, member).fetchJoin()
                .join(cart.itemSize, itemSize).fetchJoin()
                .join(cart.itemSize.item, item).fetchJoin()
                .where(cart.member.id.eq(memberId)
                        .and(cart.itemSize.id.eq(itemSizeId))
                        .and(cart.itemSize.item.itemStatus.eq(ItemStatus.ACTIVE)
                                .or(cart.itemSize.item.itemStatus.eq(ItemStatus.SOLD_OUT))))
                .fetch();
    }

    @Override
    public List<Cart> findCartDetails(Long memberId) {
        return queryFactory.selectFrom(cart)
                .join(cart.member, member).fetchJoin()
                .join(cart.itemSize, itemSize).fetchJoin()
                .join(cart.itemSize.item, item).fetchJoin()
                .where(cart.member.id.eq(memberId)
                        .and(cart.itemSize.item.itemStatus.eq(ItemStatus.ACTIVE)
                                .or(cart.itemSize.item.itemStatus.eq(ItemStatus.SOLD_OUT))))
                .fetch();
    }

    @Override
    public void deleteAllByMemberIdAndItemSizeIdIn(Long memberId, List<Long> itemSizeIds) {
        queryFactory
                .delete(cart)
                .where(cart.member.id.eq(memberId)
                        .and(cart.itemSize.id.in(itemSizeIds)))
                .execute();
    }

    @Override
    public Optional<Cart> findByMemberIdAndItemSizeId(Long memberId, Long itemSizeId) {
        Cart result = queryFactory.selectFrom(cart)
                .where(cart.member.id.eq(memberId)
                        .and(cart.itemSize.id.eq(itemSizeId)))
                .fetchOne();

        return Optional.ofNullable(result);
    }

}
