package com.example.shop.repository.discount;

import com.example.shop.domain.shop.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long>, DiscountRepositoryCustom {
}
