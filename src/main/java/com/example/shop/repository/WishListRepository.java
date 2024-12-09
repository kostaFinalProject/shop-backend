package com.example.shop.repository;

import com.example.shop.domain.shop.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByMemberId(Long member);
}
