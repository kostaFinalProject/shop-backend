package com.example.shop.controller;

import com.example.shop.aop.SecurityAspect;
import com.example.shop.service.WishListItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wish-lists")
public class WishListItemController {

    private final WishListItemService wishListItemService;

    @PostMapping("/{itemId}")
    public ResponseEntity<?> saveWishListItem(@PathVariable("itemId") Long itemId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        wishListItemService.saveWishListItem(memberId, itemId);
        return ResponseEntity.status(HttpStatus.OK).body("관심 상품으로 등록했습니다.");
    }

    @DeleteMapping("/{wishListItemId}")
    public ResponseEntity<?> deleteWishListItem(@PathVariable("wishListItemId") Long wishListItemId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        wishListItemService.deleteWishListItem(wishListItemId);
        return ResponseEntity.status(HttpStatus.OK).body("관심 상품에서 삭제했습니다.");
    }

    @GetMapping
    public ResponseEntity<?> getWishListItems() {
        Long memberId = SecurityAspect.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(wishListItemService.getWishListItems(memberId));
    }
}
