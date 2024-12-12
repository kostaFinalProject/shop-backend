package com.example.shop.repository.discount;

import com.example.shop.domain.shop.Discount;
import com.example.shop.domain.shop.ItemStatus;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.example.shop.domain.shop.QDiscount.discount;

@RequiredArgsConstructor
public class DiscountRepositoryImpl implements DiscountRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsDiscountByItemId(Long itemId) {
        return queryFactory.selectOne()
                .from(discount)
                .where(discount.item.id.eq(itemId))
                .fetchFirst() != null;
    }

    @Override
    public Optional<Discount> findDiscountByItemId(Long itemId) {
        Discount result = queryFactory.selectFrom(discount)
                .where(discount.item.id.eq(itemId)
                        .and(discount.item.itemStatus.ne(ItemStatus.DELETED)))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<Discount> findAllDiscountWithItem(Pageable pageable) {
        List<Discount> discounts = queryFactory.selectFrom(discount)
                .where(discount.item.itemStatus.ne(ItemStatus.DELETED))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(discount.count())
                .from(discount)
                .where(discount.item.itemStatus.ne(ItemStatus.DELETED));

        return PageableExecutionUtils.getPage(discounts, pageable, countQuery::fetchOne);
    }
}
