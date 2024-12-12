package com.example.shop.repository.itemcategory;

import com.example.shop.domain.shop.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long>, ItemCategoryRepositoryCustom {
    Optional<ItemCategory> findByName(String name);
}
