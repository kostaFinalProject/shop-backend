package com.example.shop.repository.discount;

import com.example.shop.domain.shop.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DiscountRepositoryCustom {
    boolean existsDiscountByItemId(Long itemId);
    Page<Discount> findAllDiscountWithItem(Pageable pageable);
    Optional<Discount> findDiscountByItemId(Long itemId);
}
