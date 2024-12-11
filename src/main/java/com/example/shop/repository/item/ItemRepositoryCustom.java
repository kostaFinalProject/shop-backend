package com.example.shop.repository.item;

import com.example.shop.domain.shop.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> searchItems(String category, String keyword, Pageable pageable);
}
