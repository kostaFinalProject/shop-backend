package com.example.shop.repository.item;

import com.example.shop.domain.shop.Item;
import com.example.shop.domain.shop.ItemStatus;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static com.example.shop.domain.shop.QDiscount.discount;
import static com.example.shop.domain.shop.QItem.item;
import static com.example.shop.domain.shop.QItemCategory.itemCategory;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Item> findByItemId(Long itemId){
        Item result = queryFactory.selectFrom(item)
                .join(item.itemCategory, itemCategory).fetchJoin()
                .where(item.id.eq(itemId)
                        .and(item.itemStatus.eq(ItemStatus.ACTIVE)
                                .or(item.itemStatus.eq(ItemStatus.SOLD_OUT))
                        ))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsNameInCategory(String category, String name) {
        return queryFactory.selectOne()
                .from(item)
                .where(item.itemCategory.name.eq(category)
                        .and(item.name.eq(name)))
                .fetchFirst() != null;
    }

    @Override
    public boolean existsNameInCategoryExceptMe(String category, String name, Long id) {
        return queryFactory.selectOne()
                .from(item)
                .where(item.itemCategory.name.eq(category)
                        .and(item.name.eq(name)
                                .and(item.id.ne(id))))
                .fetchFirst() != null;
    }

    @Override
    public Page<Item> searchItems(String category, String keyword, String sortCondition, Pageable pageable) {
        BooleanExpression keywordCondition = hasKeyword(keyword);
        BooleanExpression categoryCondition = hasCategory(category);
        BooleanExpression commonCondition = item.itemStatus.eq(ItemStatus.ACTIVE)
                .or(item.itemStatus.eq(ItemStatus.SOLD_OUT));

        JPQLQuery<Tuple> query = queryFactory.select(item, discount)
                .from(item)
                .leftJoin(discount).on(item.id.eq(discount.item.id));

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortCondition);

        List<Item> content = query.select(item)
                .where(combineConditions(commonCondition, keywordCondition, categoryCondition))
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        JPAQuery<Long> countQuery = queryFactory.select(item.count())
                .from(item)
                .where(combineConditions(commonCondition, keywordCondition, categoryCondition));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortCondition) {
        if ("newest".equals(sortCondition)) {
            return item.createAt.desc();
        } else if ("highest".equals(sortCondition)) {
            return new CaseBuilder()
                    .when(discount.isNotNull()).then(discount.discountPrice)
                    .otherwise(item.price)
                    .desc();
        } else if ("lowest".equals(sortCondition)) {
            return new CaseBuilder()
                    .when(discount.isNotNull()).then(discount.discountPrice)
                    .otherwise(item.price)
                    .asc();
        } else if ("discount".equals(sortCondition)) {
            return new CaseBuilder()
                    .when(discount.isNotNull()).then(discount.discountPercent)
                    .otherwise(0)
                    .desc();
        } else if ("order".equals(sortCondition)) {
            return item.orderCount.desc();
        }

        return item.createAt.desc();
    }
    private BooleanExpression hasKeyword(String keyword) {
        return (keyword == null || keyword.isEmpty()) ? null : item.name.containsIgnoreCase(keyword);
    }

    private BooleanExpression hasCategory(String category) {
        return (category == null || category.isEmpty()) ? null : item.itemCategory.name.eq(category);
    }

    private BooleanExpression combineConditions(BooleanExpression... conditions) {
        BooleanExpression result = null;
        for (BooleanExpression condition : conditions) {
            if (condition != null) {
                result = (result == null) ? condition : result.and(condition);
            }
        }
        return result;
    }
}
