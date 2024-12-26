package com.example.shop.repository.cart;

import com.example.shop.domain.shop.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepositoryCustom {
    List<Cart> findCartDetails(Long memberId, Long itemSizeId);
    List<Cart> findCartDetails(Long memberId);
    void deleteAllByMemberIdAndItemSizeIdIn(Long memberId, List<Long> itemSizeIds);
    Optional<Cart> findByMemberIdAndItemSizeId(Long memberId, Long itemSizeId);
}
