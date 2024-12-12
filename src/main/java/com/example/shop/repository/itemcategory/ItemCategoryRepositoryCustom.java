package com.example.shop.repository.itemcategory;

import com.example.shop.domain.shop.ItemCategory;

import java.util.List;

public interface ItemCategoryRepositoryCustom {
    List<ItemCategory> findCategoriesWithoutParentCategory();
    List<ItemCategory> findCategoriesByParentCategoryId(Long parentCategoryId);
}
