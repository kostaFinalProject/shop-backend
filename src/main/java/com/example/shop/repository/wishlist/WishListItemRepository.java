package com.example.shop.repository.wishlist;

import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.Item;
import com.example.shop.domain.shop.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListItemRepository extends JpaRepository<WishListItem, Long>, WishListItemRepositoryCustom {
    boolean existsByMemberAndItem(Member member, Item item);
}
