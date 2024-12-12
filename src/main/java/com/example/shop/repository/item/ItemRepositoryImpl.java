package com.example.shop.repository.item;

import com.example.shop.domain.shop.Item;
import com.example.shop.domain.shop.ItemStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

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
                .fetchOne() != null;
    }

    @Override
    public boolean existsNameInCategoryExceptMe(String category, String name, Long id) {
        return queryFactory.selectOne()
                .from(item)
                .where(item.itemCategory.name.eq(category)
                        .and(item.name.eq(name)
                                .and(item.id.ne(id))))
                .fetchOne() != null;
    }

    @Override
    public Page<Item> searchItems(String category, String keyword, Pageable pageable) {
        BooleanExpression keywordCondition = hasKeyword(keyword);
        BooleanExpression categoryCondition = hasCategory(category);

        List<Item> content = queryFactory.selectFrom(item)
                .where(item.itemStatus.eq(ItemStatus.ACTIVE)
                        .or(item.itemStatus.eq(ItemStatus.SOLD_OUT))
                        .and(combineConditions(keywordCondition, categoryCondition)))
                .orderBy(item.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(item.count())
                .from(item)
                .where(combineConditions(keywordCondition, categoryCondition)
                        .and(item.itemStatus.eq(ItemStatus.ACTIVE)
                                .or(item.itemStatus.eq(ItemStatus.SOLD_OUT))
                        ));

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
