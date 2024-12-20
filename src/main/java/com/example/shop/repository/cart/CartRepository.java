package com.example.shop.repository.cart;

import com.example.shop.domain.shop.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {
    void deleteAllByMemberIdAndIdIn(Long memberId, List<Long> cartItemIds);
}
