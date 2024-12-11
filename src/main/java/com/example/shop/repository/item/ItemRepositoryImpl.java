package com.example.shop.repository.item;

import com.example.shop.domain.shop.Item;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.shop.domain.shop.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Item> searchItems(String category, String keyword, Pageable pageable) {
        BooleanExpression keywordCondition = hasKeyword(keyword);
        BooleanExpression categoryCondition = hasCategory(category);

        List<Item> content = queryFactory.selectFrom(item)
                .where(combineConditions(keywordCondition, categoryCondition))
                .orderBy(item.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(item.count())
                .from(item)
                .where(combineConditions(keywordCondition, categoryCondition));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
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
