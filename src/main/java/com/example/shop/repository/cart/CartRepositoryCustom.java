package com.example.shop.repository.cart;

import com.example.shop.domain.shop.Cart;

import java.util.List;

public interface CartRepositoryCustom {
    List<Cart> findCartDetails(Long memberId, Long itemSizeId);
    List<Cart> findCartDetails(Long memberId);
}
