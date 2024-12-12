package com.example.shop.repository.itemcategory;

import com.example.shop.domain.shop.ItemCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.shop.domain.shop.QItemCategory.itemCategory;

@RequiredArgsConstructor
public class ItemCategoryRepositoryImpl implements ItemCategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ItemCategory> findCategoriesWithoutParentCategory() {
         return queryFactory.selectFrom(itemCategory)
                .where(itemCategory.parentItemCategory.isNull())
                .fetch();
    }

    @Override
    public List<ItemCategory> findCategoriesByParentCategoryId(Long parentCategoryId) {
        return queryFactory.selectFrom(itemCategory)
                .where(itemCategory.parentItemCategory.id.eq(parentCategoryId))
                .fetch();
    }
}
