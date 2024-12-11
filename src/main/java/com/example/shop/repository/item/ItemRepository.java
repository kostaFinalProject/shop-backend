package com.example.shop.repository.item;

import com.example.shop.domain.shop.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> ,ItemRepositoryCustom {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
