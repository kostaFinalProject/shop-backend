package com.example.shop.repository.wishlist;

import com.example.shop.domain.shop.WishListItem;

import java.util.List;

public interface WishListItemRepositoryCustom {
    List<WishListItem> findWishListItemByMemberId(Long memberId);
}
