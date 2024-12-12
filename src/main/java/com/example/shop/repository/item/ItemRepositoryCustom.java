package com.example.shop.repository.item;

import com.example.shop.domain.shop.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemRepositoryCustom {
    Optional<Item> findByItemId(Long id);
    boolean existsNameInCategory(String category, String name);
    boolean existsNameInCategoryExceptMe(String category, String name, Long id);
    Page<Item> searchItems(String category, String keyword, Pageable pageable);
}
